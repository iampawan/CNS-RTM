package com.mtechviral.cnsrtm;

import android.app.Application;
import android.content.ContextWrapper;

import com.mtechviral.cnsrtm.utils.Prefs;
import com.onesignal.OneSignal;

/**
 * Created by pawankumar on 26/03/17.
 */

public class CnsApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        setPref();
        OneSignal.startInit(this).init();
    }

    private void setPref(){
        new Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();
    }
}
