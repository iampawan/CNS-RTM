package com.mtechviral.cnsrtm.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mtechviral.cnsrtm.R;
import com.mtechviral.cnsrtm.adapters.PendingSpareAdapter;
import com.mtechviral.cnsrtm.apis.ApiUtils;
import com.mtechviral.cnsrtm.apis.interfaces.PendingSpareApiService;
import com.mtechviral.cnsrtm.listeners.PendingSpareClickListener;
import com.mtechviral.cnsrtm.model.PendingSparesRequest;
import com.mtechviral.cnsrtm.model.PendingSparesResponse;
import com.mtechviral.cnsrtm.model.datamodel.PendingSparesData;
import com.mtechviral.cnsrtm.utils.Prefs;
import com.mtechviral.cnsrtm.utils.Utility;

import java.util.ArrayList;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class PendingSpareActivity extends AppCompatActivity implements PendingSpareClickListener {
    RecyclerView rView;
    SwipeRefreshLayout mSwipeRefreshLayout;
    PendingSpareAdapter rcAdapter;
    ArrayList<PendingSparesData> allItems = new ArrayList<>();
    PendingSpareApiService pendingSpareApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_spare);
        initToolbaritems();
        initComponents();
    }
    private void initToolbaritems() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.requests);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initComponents() {

        rView = (RecyclerView) findViewById(R.id.recyclerView);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        setComponents();
    }

    private void setComponents(){
        pendingSpareApiService = ApiUtils.getPendingSpareAPIService();
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        rView.setLayoutManager(layoutManager);
        rView.setNestedScrollingEnabled(false);
        rView.setHasFixedSize(false);
        rcAdapter = new PendingSpareAdapter(this, new ArrayList<PendingSparesData>());
        rView.setAdapter(rcAdapter);

        loadJSON();

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                rcAdapter.resetListData();
                loadJSON();

            }
        });
    }

    private void setRecyclerView(ArrayList<PendingSparesData> rowListItem) {

        rcAdapter = new PendingSpareAdapter(this, rowListItem);
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
                requestSparesData();
            }
        }, 100);

    }

    private void requestSparesData() {
        final PendingSparesRequest spareRequest = new PendingSparesRequest(Prefs.getString("token", ""),"-1");

        pendingSpareApiService.savePost(spareRequest).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<PendingSparesResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        onFailRequest(e.getMessage());

                    }

                    @Override
                    public void onNext(PendingSparesResponse pendingSparesResponse) {
                        if (pendingSparesResponse.getMessage().equals("Success")) {
                            displayApiResult(pendingSparesResponse);
                        } else {
                            onFailRequest(getString(R.string.something_wrong));
                        }
                    }

                });
    }

    private void displayApiResult(PendingSparesResponse pendingSparesResponse) {
        Log.d("spare - ",pendingSparesResponse.toString());
        allItems.clear();
        swipeProgress(false);
        int datasize = pendingSparesResponse.getData().size();
        if (datasize == 0) {
            showNoItemView(true);
        } else {
            for (int i = 0; i < datasize; i++) {
                allItems.add(i, pendingSparesResponse.getData().get(i));
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }
}
