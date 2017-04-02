package com.mtechviral.cnsrtm.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
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
import com.mtechviral.cnsrtm.databinding.User;
import com.mtechviral.cnsrtm.utils.Prefs;
import com.mtechviral.cnsrtm.utils.Utility;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SignInActivity extends AppCompatActivity {
    public static final String TAG = SignInActivity.class.getSimpleName();
    ActivityMainBinding activityMainBinding;
    LoginApiService loginApiService;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        User user = new User();
        activityMainBinding.setUser(user);
        activityMainBinding.setActivity(this);

        //check login

        checkLoggedIn();

        setToolbar();
        loadBG();
        //getApiService
        loginApiService = ApiUtils.getLoginAPIService();
    }

    private void checkLoggedIn(){
        if(Prefs.getBoolean("loggedIn",false)){
            if(Prefs.getBoolean("admin",false)) {
                startActivity(new Intent(this, AdminHomeActivity.class));
                finish();
            }else{
                startActivity(new Intent(this, HomeActivity.class));
                finish();
            }
        }
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
                        activityMainBinding.airportView.setBackground(resource);
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

        } else {
            Toast.makeText(this, R.string.enter_all_fields, Toast.LENGTH_SHORT).show();
        }
    }
    public void onAdminLoginClicked(String username, String password) {
        if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
            if (Utility.isNetworkConnected(this)) {
                sendAdminPost(username.trim(), password.trim());
            } else {
                Toast.makeText(this, R.string.check_internet, Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(this, R.string.enter_all_fields, Toast.LENGTH_SHORT).show();
        }
    }

    public void sendPost(String username, String password) {
        final ProgressDialog pd = Utility.showProgress(this,R.string.please_wait);
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
                        Toast.makeText(SignInActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onNext(LoginResponse loginResponse) {
                        pd.dismiss();
                        String token = loginResponse.getToken();
                        if(activityMainBinding.cbRemember.isChecked()) {
                            Prefs.putBoolean("loggedIn", true);
                            Prefs.putBoolean("admin", false);
                        }
                        Prefs.putString("token",token);
                        Prefs.putString("location",loginResponse.getLocation());
                        Prefs.putString("firstname",loginResponse.getFirstname());
                        Prefs.putString("lastname",loginResponse.getLastname());
                        Prefs.putString("imageurl",loginResponse.getImageurl());

                        Log.d(TAG, "onNext: Token"+token);
                        Intent i = new Intent(SignInActivity.this, HomeActivity.class);
                        startActivity(i);
                        finish();

                    }

                });

    }
    public void sendAdminPost(String username, String password) {
        final ProgressDialog pd = Utility.showProgress(this,R.string.please_wait);
        final LoginRequest loginPost = new LoginRequest(username, password);

        loginApiService.saveAdminPost(loginPost).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<LoginResponse>() {
                    @Override
                    public void onCompleted() {
                        pd.dismiss();

                    }
                    @Override
                    public void onError(Throwable e) {
                        pd.dismiss();
                        Toast.makeText(SignInActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onNext(LoginResponse loginResponse) {
                        pd.dismiss();
                        String token = loginResponse.getToken();
                        if(activityMainBinding.cbRemember.isChecked()) {
                            Prefs.putBoolean("loggedIn", true);
                            Prefs.putBoolean("admin", true);
                        }
                        Prefs.putString("token",token);
                        Prefs.putString("location",loginResponse.getLocation());
                        Prefs.putString("firstname",loginResponse.getFirstname());
                        Prefs.putString("lastname",loginResponse.getLastname());
                        Prefs.putString("imageurl",loginResponse.getImageurl());
                        Log.d(TAG, "onNext: Token"+token);
                        Intent i = new Intent(SignInActivity.this, AdminHomeActivity.class);
                        startActivity(i);
                        finish();

                    }

                });

    }

}
