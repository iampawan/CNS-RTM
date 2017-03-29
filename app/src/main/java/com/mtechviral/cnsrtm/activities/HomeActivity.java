package com.mtechviral.cnsrtm.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import com.mtechviral.cnsrtm.R;
import com.mtechviral.cnsrtm.adapters.EquipmentAdapter;
import com.mtechviral.cnsrtm.apis.ApiUtils;
import com.mtechviral.cnsrtm.apis.interfaces.MaterialListApiService;
import com.mtechviral.cnsrtm.listeners.EquipmentClickListener;
import com.mtechviral.cnsrtm.model.EquipmentRequest;
import com.mtechviral.cnsrtm.model.EquipmentResponse;
import com.mtechviral.cnsrtm.model.datamodel.EquipmentData;
import com.mtechviral.cnsrtm.utils.Prefs;
import com.mtechviral.cnsrtm.utils.Utility;

import java.util.ArrayList;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class HomeActivity extends AppCompatActivity implements EquipmentClickListener, View.OnClickListener {
    MaterialListApiService materialListApiService;
    RecyclerView rView;
    SwipeRefreshLayout mSwipeRefreshLayout;
    EquipmentAdapter rcAdapter;
    ArrayList<EquipmentData> allItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initToolbar();
        initNavigation();
        initComponents();

    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getString(R.string.airport_authority_of_india));
            actionBar.setDisplayShowTitleEnabled(true);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void initNavigation() {
        final NavigationView navigationViewLeft = (NavigationView) findViewById(R.id.nav_view);
        View navLeftLay = navigationViewLeft.getHeaderView(0);
        Space spaceLeftTop = (Space) navLeftLay.findViewById(R.id.spaceLeftTop);
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            spaceLeftTop.setVisibility(View.VISIBLE);
        }
    }

    private void initComponents() {
        rView = (RecyclerView) findViewById(R.id.recyclerView);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeToRefresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rView.setLayoutManager(layoutManager);
        rView.setNestedScrollingEnabled(false);
        rView.setHasFixedSize(false);
        rcAdapter = new EquipmentAdapter(this, new ArrayList<EquipmentData>());
        rView.setAdapter(rcAdapter);

        materialListApiService = ApiUtils.getMaterialListAPIService();

        loadJSON();

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                rcAdapter.resetListData();
                loadJSON();

            }
        });
    }

    private void setRecyclerView(ArrayList<EquipmentData> rowListItem) {

        rcAdapter = new EquipmentAdapter(this, rowListItem);
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
        final ProgressDialog pd = Utility.showProgress(this, R.string.please_wait);
        final EquipmentRequest equipmentRequest = new EquipmentRequest(Prefs.getString("token", ""));

        materialListApiService.savePost(equipmentRequest).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<EquipmentResponse>() {
                    @Override
                    public void onCompleted() {
                        pd.dismiss();

                    }

                    @Override
                    public void onError(Throwable e) {
                        pd.dismiss();
                        onFailRequest(e.getMessage().toString());

                    }

                    @Override
                    public void onNext(EquipmentResponse equipmentResponse) {
                        pd.dismiss();
                        if (equipmentResponse.getMessage().equals("Success")) {
                            displayApiResult(equipmentResponse);
                        } else {
                            onFailRequest(getString(R.string.something_wrong));
                        }
                    }

                });
    }

    private void displayApiResult(EquipmentResponse equipmentResponse) {
        allItems.clear();
        swipeProgress(false);
        int datasize = equipmentResponse.getData().size();
        if (datasize == 0) {
            showNoItemView(true);
        } else {
            for (int i = 0; i < datasize; i++) {
                allItems.add(i, equipmentResponse.getData().get(i));
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
        Toast.makeText(HomeActivity.this, "Position " + num + " clicked!", Toast.LENGTH_SHORT).show();
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLoginSignupBack:
                onBackPressed();
                break;
            default:
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }
}
