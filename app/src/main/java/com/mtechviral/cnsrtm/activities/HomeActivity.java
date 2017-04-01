package com.mtechviral.cnsrtm.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.mtechviral.cnsrtm.R;
import com.mtechviral.cnsrtm.adapters.EquipmentAdapter;
import com.mtechviral.cnsrtm.apis.ApiUtils;
import com.mtechviral.cnsrtm.apis.interfaces.MaterialCreateApiService;
import com.mtechviral.cnsrtm.apis.interfaces.MaterialListApiService;
import com.mtechviral.cnsrtm.listeners.EquipmentClickListener;
import com.mtechviral.cnsrtm.model.EquipmentRequest;
import com.mtechviral.cnsrtm.model.EquipmentResponse;
import com.mtechviral.cnsrtm.model.MaterialCreateRequest;
import com.mtechviral.cnsrtm.model.MaterialCreateResponse;
import com.mtechviral.cnsrtm.model.datamodel.EquipmentData;
import com.mtechviral.cnsrtm.utils.Prefs;
import com.mtechviral.cnsrtm.utils.Utility;

import java.util.ArrayList;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class HomeActivity extends AppCompatActivity implements EquipmentClickListener {
    MaterialListApiService materialListApiService;
    RecyclerView rView;
    SwipeRefreshLayout mSwipeRefreshLayout;
    EquipmentAdapter rcAdapter;
    ArrayList<EquipmentData> allItems = new ArrayList<>();
    ImageView profileView;
    AppBarLayout appBarLayout;
    LinearLayout bgLinear;
    TextView tvAdminName,tvAdminLocation;
    MaterialCreateApiService materialCreateApiService;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initToolbaritems();
        initNavigation();
        initComponents();


    }

    private void initToolbaritems() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        appBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout);
        bgLinear = (LinearLayout) findViewById(R.id.bgLinear);
        profileView = (ImageView) findViewById(R.id.profileView);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        String url = "";
        String urlThumb = "https://s3-us-west-2.amazonaws.com/material-ui-template/" + "profile/style-3/Profile-3-header-thumb.png";

        loadImageRequest(drawer, url, urlThumb);
    }

    private void loadImageRequest(final DrawerLayout bg, String url, String urlThumb) {
//        Glide.with(this).load(Prefs.getString("imageurl","")).asBitmap().centerCrop().placeholder(R.drawable.loading_placeholder).into(new BitmapImageViewTarget(profileView) {
//            @Override
//            protected void setResource(Bitmap resource) {
//                RoundedBitmapDrawable circularBitmapDrawable =
//                        RoundedBitmapDrawableFactory.create(getResources(), resource);
//                circularBitmapDrawable.setCircular(true);
//                profileView.setImageDrawable(circularBitmapDrawable);
//            }
//        });
        String fn = Prefs.getString("firstname","").substring(0,1);
        String ln = Prefs.getString("lastname","").substring(0,1);
        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
// generate random color
        int color1 = generator.getRandomColor();

        TextDrawable drawable = TextDrawable.builder()
                .buildRound(fn+ln, color1);

        profileView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        profileView.setImageDrawable(drawable);

        DrawableRequestBuilder<String> thumbnailRequest = Glide
                .with(this)
                .load(urlThumb);

        Glide.with(this)
                .load(R.drawable.plane)
                .crossFade()
                .centerCrop()
                .into(new SimpleTarget<GlideDrawable>() {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                       bg.setBackgroundColor(getResources().getColor(R.color.blackone));
                    }
                });
    }


    private void initNavigation() {
        final NavigationView navigationViewLeft = (NavigationView) findViewById(R.id.nav_view);
//        View navLeftLay = navigationViewLeft.getHeaderView(0);
        navigationViewLeft.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(final MenuItem item) {
                onItemSelected(item.getItemId());
                //drawer.closeDrawers();
                return true;
            }
        });
        navigationViewLeft.setItemIconTintList(getResources().getColorStateList(R.color.nav_state_list));
    }

    private void initComponents() {
        tvAdminName = (TextView) findViewById(R.id.tvAdminName);
        tvAdminLocation = (TextView) findViewById(R.id.tvAdminLocation);
        rView = (RecyclerView) findViewById(R.id.recyclerView);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeToRefresh);
        setComponents();


    }

    private void setComponents(){
        materialListApiService = ApiUtils.getMaterialListAPIService();
        materialCreateApiService = ApiUtils.getMaterialCreateAPIService();
        tvAdminName.setText(Prefs.getString("firstname","")+" "+Prefs.getString("lastname",""));
        tvAdminLocation.setText(Prefs.getString("location",""));
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        rView.setLayoutManager(layoutManager);
        rView.setNestedScrollingEnabled(false);
        rView.setHasFixedSize(false);
        rcAdapter = new EquipmentAdapter(this, new ArrayList<EquipmentData>());
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
        final EquipmentRequest equipmentRequest = new EquipmentRequest(Prefs.getString("token", ""));

        materialListApiService.savePost(equipmentRequest).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<EquipmentResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        onFailRequest(e.getMessage().toString());

                    }

                    @Override
                    public void onNext(EquipmentResponse equipmentResponse) {
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
    public void itemClicked(View view, final int position) {
        int num = position + 1;
        final String name = allItems.get(position).getMaterialName();
        final Integer id = allItems.get(position).getId();
        Log.d("aa", "itemClicked: "+name+"-"+id);

        new MaterialDialog.Builder(this)
                .title(R.string.choose)
                .items(R.array.material_options)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        dialog.dismiss();
                        Log.d("which1", "onSelection: "+which);
                        if(text.equals("View all spares")){
                            Intent i = new Intent(HomeActivity.this, SpareActivity.class);
                            i.putExtra("mat_id",id);
                            i.putExtra("mat_name",name);
                            i.putExtra("mat_image",allItems.get(position).getImageUrl());
                            startActivity(i);
                        }else {
                            new MaterialDialog.Builder(HomeActivity.this)
                                    .title(R.string.choose)
                                    .items(R.array.material_detail_option)
                                    .itemsCallback(new MaterialDialog.ListCallback() {
                                        @Override
                                        public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                            dialog.dismiss();
                                            Log.d("which2", "onSelection: "+which);
                                            callMaterialOption(which+1,id,name);
                                        }
                                    })
                                    .show();
                        }
                    }
                })
                .show();


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





//    @Override
//    public boolean onCreateOptionsMenu(final Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_home,menu);
//        final MenuItem searchItem = menu.findItem(R.id.action_search);
//        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
//        searchView.setIconified(false);
//        searchView.setQueryHint("Search item...");
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String s) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String s) {
//                try {
//                    rcAdapter.getFilter().filter(s);
//                } catch (Exception e) {
//                }
//                return true;
//            }
//        });
//        // Detect SearchView icon clicks
//        searchView.setOnSearchClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                itemTouchHelper.attachToRecyclerView(null);
//                setItemsVisibility(menu, searchItem, false);
//            }
//        });
//
//        // Detect SearchView close
//        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
//            @Override
//            public boolean onClose() {
////                itemTouchHelper.attachToRecyclerView(rView);
//                setItemsVisibility(menu, searchItem, true);
//                return false;
//            }
//        });
//        searchView.onActionViewCollapsed();
//        return super.onCreateOptionsMenu(menu);
//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_qr:
                Intent i = new Intent(this, QrActivity.class);
                startActivity(i);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setItemsVisibility(Menu menu, MenuItem exception, boolean visible) {
        for (int i=0; i<menu.size(); ++i) {
            MenuItem item = menu.getItem(i);
            if (item != exception) item.setVisible(visible);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            doExitApp();
        }
    }

    private long exitTime = 0;
    public void doExitApp() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(this, R.string.press_again_exit_app, Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }

    public boolean onItemSelected(int id) {
        switch (id) {
            //sub menu
            case R.id.requests:
                Intent i = new Intent(this, PendingSpareActivity.class);
                startActivity(i);
                break;
            case R.id.create_service:
                startActivity(new Intent(this, CreateServiceActivity.class));
                break;
            default:
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawers();
        return true;
    }

    private void callMaterialOption(Integer req_type, Integer id, String name){

        pd = Utility.showProgress(this,R.string.please_wait);
        final MaterialCreateRequest materialCreateRequest = new MaterialCreateRequest(Prefs.getString("token", ""),id,req_type);

        materialCreateApiService.savePost(materialCreateRequest).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<MaterialCreateResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        onFailRequest2(e.getMessage());

                    }

                    @Override
                    public void onNext(MaterialCreateResponse materialCreateResponse) {
                        if (materialCreateResponse.getMessage().equals("Success")) {
                            displayApiResult2(materialCreateResponse);
                        } else {
                            onFailRequest2(getString(R.string.something_wrong));
                        }
                    }

                });
    }

    private void displayApiResult2(MaterialCreateResponse materialCreateResponse) {
        pd.dismiss();
        new AlertDialog.Builder(this)
                .setTitle("Success")
                .setMessage("Your request id is: "+materialCreateResponse.getReqId()+". It will be processed shortly.")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
//                        Intent i = new Intent(HomeActivity.this, HomeActivity.class);
//                        startActivity(i);
//                        finish();
                    }
                })
                .show();
    }

    private void onFailRequest2(String message) {
        pd.dismiss();
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    }


