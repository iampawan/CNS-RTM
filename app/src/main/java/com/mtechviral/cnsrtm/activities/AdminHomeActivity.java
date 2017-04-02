package com.mtechviral.cnsrtm.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.mtechviral.cnsrtm.R;
import com.mtechviral.cnsrtm.adapters.ApproveAdapter;
import com.mtechviral.cnsrtm.adapters.AssignListAdapter;
import com.mtechviral.cnsrtm.apis.ApiUtils;
import com.mtechviral.cnsrtm.apis.interfaces.ApproveApiService;
import com.mtechviral.cnsrtm.apis.interfaces.AssignListApiService;
import com.mtechviral.cnsrtm.apis.interfaces.AssignNowApiService;
import com.mtechviral.cnsrtm.listeners.ApprovedItemClickListener;
import com.mtechviral.cnsrtm.listeners.AssignListClickListener;
import com.mtechviral.cnsrtm.model.ApproveRequest;
import com.mtechviral.cnsrtm.model.ApproveResponse;
import com.mtechviral.cnsrtm.model.AssignListRequest;
import com.mtechviral.cnsrtm.model.AssignListResponse;
import com.mtechviral.cnsrtm.model.AssignMaterial;
import com.mtechviral.cnsrtm.model.AssignNowRequest;
import com.mtechviral.cnsrtm.model.AssignNowResponse;
import com.mtechviral.cnsrtm.model.datamodel.ApproveData;
import com.mtechviral.cnsrtm.utils.Prefs;
import com.mtechviral.cnsrtm.utils.Utility;

import java.util.ArrayList;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AdminHomeActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Admin RTM");

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);


    }


    public static class PlaceholderFragment extends Fragment implements ApprovedItemClickListener,AssignListClickListener {

        private static final String ARG_SECTION_NUMBER = "section_number";
        ApproveApiService approveApiService;
        AssignListApiService assignListApiService;
        AssignNowApiService assignNowApiService;
        RecyclerView rView;
        SwipeRefreshLayout mSwipeRefreshLayout;
        ApproveAdapter rcAdapter;
        AssignListAdapter rcAdapter2;
        ArrayList<ApproveData> allItems = new ArrayList<>();
        ArrayList<AssignMaterial> mAllItems = new ArrayList<>();
        ProgressDialog pd;
        Boolean s = false;


        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView;
            if(getArguments().getInt(ARG_SECTION_NUMBER) == 1) {
                 rootView = inflater.inflate(R.layout.fragment_approve, container, false);
                rView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
                mSwipeRefreshLayout = (SwipeRefreshLayout)rootView.findViewById(R.id.swipe_refresh_layout);
                setComponents(rootView,1);

            }else {
                rootView = inflater.inflate(R.layout.fragment_assign, container, false);
                rView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
                mSwipeRefreshLayout = (SwipeRefreshLayout)rootView.findViewById(R.id.swipe_refresh_layout);
                setComponents(rootView,2);

            }

            return rootView;
        }

        private void setComponents(final View rootview, final Integer i){
            approveApiService = ApiUtils.getAprooveAPIService();
            assignListApiService = ApiUtils.getAssignListAPIService();
            assignNowApiService = ApiUtils.getAssignNowAPIService();
            mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
            rView.setLayoutManager(layoutManager);
            rView.setNestedScrollingEnabled(false);
            rView.setHasFixedSize(false);
            if(i == 1) {
                rcAdapter = new ApproveAdapter(getActivity(), new ArrayList<ApproveData>());
                rView.setAdapter(rcAdapter);
            }else{
                rcAdapter2 = new AssignListAdapter(getActivity(), new ArrayList<AssignMaterial>());
                rView.setAdapter(rcAdapter2);
            }



            loadJSON(rootview,i);

            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    if(i == 1) {
                        rcAdapter.resetListData();
                    }else {
                        rcAdapter2.resetListData();
                    }
                    loadJSON(rootview,i);

                }
            });

        }

        private void setRecyclerView(ArrayList<ApproveData> rowListItem) {

            rcAdapter = new ApproveAdapter(getActivity(), rowListItem);
            rView.setAdapter(rcAdapter);
            rcAdapter.setClickListener(this);
        }
        private void setRecyclerView2(ArrayList<AssignMaterial> rowListItem) {

            rcAdapter2 = new AssignListAdapter(getActivity(), rowListItem);
            rView.setAdapter(rcAdapter2);
            rcAdapter2.setClickListener(this);
        }

        private void loadJSON(final View rootview, final Integer i) {
            showFailedView(false, "",rootview,i);
            showNoItemView(false,rootview,i);
            swipeProgress(true);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(i == 1) {
                        requestPendingData(rootview,i);
                    }else{
                        requestAssignList(rootview,i);
                    }
                }
            }, 2000);

        }
        private void requestPendingData(final View rootview, final Integer i){
            final ApproveRequest approveRequest = new ApproveRequest(Prefs.getString("token", ""));

            approveApiService.savePost(approveRequest).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<ApproveResponse>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            onFailRequest(e.getMessage(),rootview,i);

                        }

                        @Override
                        public void onNext(ApproveResponse approveResponse) {
                            if (approveResponse.getMessage().equals("Success")) {
                                displayApiResult(approveResponse,rootview,i);
                            } else {
                                onFailRequest(getString(R.string.something_wrong),rootview,i);
                            }
                        }

                    });
        }

        private void requestAssignList(final View rootview,final Integer i){
            final AssignListRequest assignListRequest = new AssignListRequest(Prefs.getString("token", ""));

            assignListApiService.savePost(assignListRequest).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<AssignListResponse>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            onFailRequest(e.getMessage(),rootview,i);

                        }

                        @Override
                        public void onNext(AssignListResponse assignListResponse) {
                            if (assignListResponse.getMessage().equals("Success")) {
                                displayApiResult2(assignListResponse,rootview,i);
                            } else {
                                onFailRequest(getString(R.string.something_wrong),rootview,i);
                            }
                        }

                    });
        }

        private void displayApiResult(ApproveResponse approveResponse,View rootView,Integer j) {
            allItems.clear();
            swipeProgress(false);
            int datasize = approveResponse.getData().size();
            if (datasize == 0) {
                showNoItemView(true,rootView,j);
            } else {
                for (int i = 0; i < datasize; i++) {
                    allItems.add(i, approveResponse.getData().get(i));
                }
                setRecyclerView(allItems);
            }
        }
        private void displayApiResult2(AssignListResponse assignListResponse,View rootView,Integer j) {
            mAllItems.clear();
            swipeProgress(false);
            int datasize = assignListResponse.getAssignMaterial().size();
            if (datasize == 0) {
                showNoItemView(true,rootView,j);
            } else {
                for (int i = 0; i < datasize; i++) {
                    mAllItems.add(i, assignListResponse.getAssignMaterial().get(i));
                }
                setRecyclerView2(mAllItems);
            }
        }

        private void onFailRequest(String message,View rootView,Integer j) {
            swipeProgress(false);
            try {
                if (Utility.isNetworkConnected(getActivity())) {
                    showFailedView(true, message, rootView, j);
                } else {
                    showFailedView(true, getString(R.string.check_internet), rootView, j);
                }
            }catch (Exception e){}
        }

        private void showFailedView(boolean show, String message, final View rootView, final Integer j) {
            View lyt_failed = rootView.findViewById(R.id.lyt_failed);
            ((TextView) rootView.findViewById(R.id.failed_message)).setText(message);
            if (show) {
                rView.setVisibility(View.GONE);
                lyt_failed.setVisibility(View.VISIBLE);
            } else {
                rView.setVisibility(View.VISIBLE);
                lyt_failed.setVisibility(View.GONE);
            }
            ((Button) rootView.findViewById(R.id.failed_retry)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    loadJSON(rootView,j);
                }
            });
        }

        private void showNoItemView(boolean show, View rootView,Integer j) {
            View lyt_no_item = (View) rootView.findViewById(R.id.lyt_no_item);
            ((TextView) rootView.findViewById(R.id.no_item_message)).setText(R.string.no_item);
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
        public void itemClicked(final View view, int position) {
            switch (view.getId()){
                case R.id.btnApproved :
                    final Integer id = allItems.get(position).getId();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            s = false;
                            Boolean success = changeApproved(id,view);
                            if(success){
                                view.setBackgroundColor(getContext().getResources().getColor(R.color.button_green));
                                view.setFocusable(false);
                                view.setClickable(false);
                            }
                        }
                    }, 2000);


                    break;

            }
        }

        private Boolean changeApproved(Integer id, final View view){

            pd = Utility.showProgress(getActivity(),R.string.please_wait);
            final ApproveRequest approveRequest = new ApproveRequest(Prefs.getString("token", ""),id);

            approveApiService.changePost(approveRequest).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<ApproveResponse>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            s = false;
                            pd.dismiss();
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onNext(ApproveResponse approveResponse) {
                            pd.dismiss();
                            if (approveResponse.getMessage().equals("Success")) {
                                Toast.makeText(getActivity(), "Done.Now swipe up to refresh.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(),R.string.something_wrong, Toast.LENGTH_SHORT).show();
                            }
                        }

                    });
        return s;
        }

        @Override
        public void itemRClicked(View view, int position) {
            final String type = mAllItems.get(position).getType();
            final String id = mAllItems.get(position).getId().toString();
            new MaterialDialog.Builder(getActivity())
                    .title(R.string.input_email)
                    .content(R.string.input_email_content)
                    .inputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS)
                    .input(R.string.input_email_hint, R.string.input_prefill, new MaterialDialog.InputCallback() {
                        @Override
                        public void onInput(MaterialDialog dialog, CharSequence input) {
                            // Do something
                            dialog.dismiss();
                            String email = input.toString();
                            assignNow(id,type,email);
                        }
                    }).show();

        }

        private void assignNow(String id, String type, String email){
            pd = Utility.showProgress(getActivity(),R.string.please_wait);
            final AssignNowRequest assignNowRequest = new AssignNowRequest(Prefs.getString("token", ""),id,type,email);

            assignNowApiService.savePost(assignNowRequest).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<AssignNowResponse>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            onFail(e.getMessage());

                        }

                        @Override
                        public void onNext(AssignNowResponse assignNowResponse) {
                            if (assignNowResponse.getMessage().equals("Success")) {
                                displayResult(assignNowResponse);
                            } else {
                                onFail(getString(R.string.something_wrong));
                            }
                        }

                    });
        }
        private void displayResult(AssignNowResponse assignNowResponse) {
            pd.dismiss();
            new AlertDialog.Builder(getActivity())
                    .setTitle("Success")
                    .setMessage("Assigned & an email sent successfully.")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Toast.makeText(getActivity(), "Swipe up to refresh status.", Toast.LENGTH_SHORT).show();
//                        Intent i = new Intent(QrActivity.this, HomeActivity.class);
//                        startActivity(i);
//                        finish();
                        }
                    })
                    .show();
        }

        private void onFail(String message) {
            pd.dismiss();
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        }
    }



    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Approve";
                case 1:
                    return "Assign";
            }
            return null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_admin_home,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_logout:
                Prefs.clear();
                Intent i = new Intent(this,SignInActivity .class);
                startActivity(i);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
