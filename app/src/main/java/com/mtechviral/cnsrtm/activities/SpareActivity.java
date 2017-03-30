package com.mtechviral.cnsrtm.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mtechviral.cnsrtm.R;
import com.mtechviral.cnsrtm.adapters.SpareAdapter;
import com.mtechviral.cnsrtm.apis.ApiUtils;
import com.mtechviral.cnsrtm.apis.interfaces.SpareListApiService;
import com.mtechviral.cnsrtm.listeners.SpareClickListener;
import com.mtechviral.cnsrtm.model.SpareRequest;
import com.mtechviral.cnsrtm.model.SpareResponse;
import com.mtechviral.cnsrtm.model.datamodel.SpareData;
import com.mtechviral.cnsrtm.utils.Prefs;
import com.mtechviral.cnsrtm.utils.Utility;

import java.util.ArrayList;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SpareActivity extends AppCompatActivity implements SpareClickListener {
    SpareListApiService spareListApiService;
    RecyclerView rView;
    SwipeRefreshLayout mSwipeRefreshLayout;
    SpareAdapter rcAdapter;
    ArrayList<SpareData> allItems = new ArrayList<>();
    Integer mat_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spare);
        initToolbaritems();
        initComponents();
        mat_id = getIntent().getExtras().getInt("mat_id");

    }

    private void initToolbaritems() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initComponents() {
        rView = (RecyclerView) findViewById(R.id.recyclerView);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        rView.setLayoutManager(layoutManager);
        rView.setNestedScrollingEnabled(false);
        rView.setHasFixedSize(false);
        rcAdapter = new SpareAdapter(this, new ArrayList<SpareData>());
        rView.setAdapter(rcAdapter);

        spareListApiService = ApiUtils.getSpareListAPIService();

        loadJSON();

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                rcAdapter.resetListData();
                loadJSON();

            }
        });

    }
    private void setRecyclerView(ArrayList<SpareData> rowListItem) {

        rcAdapter = new SpareAdapter(this, rowListItem);
        rView.setAdapter(rcAdapter);
        rcAdapter.setClickListener(this);
    }

    private void loadJSON() {
        showFailedView(false, "");
        showNoItemView(false);
        swipeProgress(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                requestMaterialData();
            }
        }, 2000);

    }

    private void requestMaterialData() {
        final SpareRequest spareRequest = new SpareRequest(Prefs.getString("token", ""),mat_id);

        spareListApiService.savePost(spareRequest).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SpareResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        onFailRequest(e.getMessage().toString());

                    }

                    @Override
                    public void onNext(SpareResponse spareResponse) {
                        if (spareResponse.getMessage().equals("Success")) {
                            displayApiResult(spareResponse);
                        } else {
                            onFailRequest(getString(R.string.something_wrong));
                        }
                    }

                });
    }

    private void displayApiResult(SpareResponse spareResponse) {
        allItems.clear();
        swipeProgress(false);
        int datasize = spareResponse.getData().size();
        if (datasize == 0) {
            showNoItemView(true);
        } else {
            for (int i = 0; i < datasize; i++) {
                allItems.add(i, spareResponse.getData().get(i));
            }
            setRecyclerView(allItems);
        }
    }

    private void onFailRequest(String message) {
        swipeProgress(false);
        if (Utility.isNetworkConnected(this)) {
            showFailedView(true, message);
        } else {
            showFailedView(true, getString(R.string.check_internet));
        }
    }

    @Override
    public void itemClicked(View view, int position) {
        int num = position + 1;
        String name = allItems.get(position).getName();
        String id = allItems.get(position).getId().toString();
        Log.d("aa", "itemClicked: "+name+"-"+id);
        Intent i = new Intent(this, ItemDetailActivity.class);
        startActivity(i);
    }

    private void showFailedView(boolean show, String message) {
        View lyt_failed = findViewById(R.id.lyt_failed);
        ((TextView) findViewById(R.id.failed_message)).setText(message);
        if (show) {
            rView.setVisibility(View.GONE);
            lyt_failed.setVisibility(View.VISIBLE);
        } else {
            rView.setVisibility(View.VISIBLE);
            lyt_failed.setVisibility(View.GONE);
        }
        ((Button) findViewById(R.id.failed_retry)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadJSON();
            }
        });
    }

    private void showNoItemView(boolean show) {
        View lyt_no_item = (View) findViewById(R.id.lyt_no_item);
        ((TextView) findViewById(R.id.no_item_message)).setText(R.string.no_item);
        if (show) {
            rView.setVisibility(View.GONE);
            lyt_no_item.setVisibility(View.VISIBLE);
        } else {
            rView.setVisibility(View.VISIBLE);
            lyt_no_item.setVisibility(View.GONE);
        }
    }

    private void swipeProgress(final boolean show) {
        if (!show) {
            mSwipeRefreshLayout.setRefreshing(show);
            return;
        }
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(show);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setIconified(false);
        searchView.setQueryHint("Search item...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                try {
                    rcAdapter.getFilter().filter(s);
                } catch (Exception e) {
                }
                return true;
            }
        });
        // Detect SearchView icon clicks
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                itemTouchHelper.attachToRecyclerView(null);
                setItemsVisibility(menu, searchItem, false);
            }
        });

        // Detect SearchView close
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
//                itemTouchHelper.attachToRecyclerView(rView);
                setItemsVisibility(menu, searchItem, true);
                return false;
            }
        });
        searchView.onActionViewCollapsed();
        return super.onCreateOptionsMenu(menu);
    }

    private void setItemsVisibility(Menu menu, MenuItem exception, boolean visible) {
        for (int i=0; i<menu.size(); ++i) {
            MenuItem item = menu.getItem(i);
            if (item != exception) item.setVisible(visible);
        }
    }
}
