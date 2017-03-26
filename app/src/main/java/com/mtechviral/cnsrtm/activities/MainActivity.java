package com.mtechviral.cnsrtm.activities;

import android.app.ProgressDialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.mtechviral.cnsrtm.R;
import com.mtechviral.cnsrtm.apis.ApiUtils;
import com.mtechviral.cnsrtm.apis.interfaces.LoginApiService;
import com.mtechviral.cnsrtm.databinding.ActivityMainBinding;
import com.mtechviral.cnsrtm.model.LoginRequest;
import com.mtechviral.cnsrtm.model.LoginResponse;
import com.mtechviral.cnsrtm.model.datamodel.User;
import com.mtechviral.cnsrtm.utils.Utility;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();
    ActivityMainBinding activityMainBinding;
    LoginApiService loginApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        User user = new User();
        activityMainBinding.setUser(user);
        activityMainBinding.setActivity(this);
        setToolbar();
        loadBG();
        //getApiService
        loginApiService = ApiUtils.getLoginAPIService();
    }

    private void setToolbar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setTitle(R.string.login);
        }
    }

    private void loadBG() {
        Glide.with(this)
                .load(R.drawable.airport)
                .thumbnail(0.01f)
                .centerCrop()
                .into(new SimpleTarget<GlideDrawable>() {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        activityMainBinding.activityBg.setBackground(resource);
                    }
                });
    }

    public void onLoginClicked(String username, String password) {
        if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
            if (Utility.isNetworkConnected(this)) {
                sendPost(username.trim(), password.trim());
            } else {
                Toast.makeText(this, R.string.check_internet, Toast.LENGTH_SHORT).show();
            }

        }
        else {
            Toast.makeText(this,R.string.enter_all_fields, Toast.LENGTH_SHORT).show();
        }
    }

    public void sendPost(String username, String password) {
        final ProgressDialog pd = Utility.showProgress(this);
        final LoginRequest loginPost = new LoginRequest(username, password);
        loginApiService.savePost(loginPost).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<LoginResponse>() {
                    @Override
                    public void onCompleted() {
                        pd.dismiss();

                    }

                    @Override
                    public void onError(Throwable e) {
                        pd.dismiss();
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onNext(LoginResponse loginResponse) {
                        pd.dismiss();
                        Toast.makeText(MainActivity.this, loginResponse.toString(), Toast.LENGTH_SHORT).show();

                    }

                });
    }

}
