package com.mtechviral.cnsrtm.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;

import com.mtechviral.cnsrtm.R;

/**
 * Created by pawankumar on 09/03/17.
 */

public class Utility {
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    public static ProgressDialog showProgress(Context context, int message){
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle(R.string.please_wait);
        progressDialog.setMessage(context.getString(message));
        progressDialog.setCancelable(false);
        progressDialog.show();
        return progressDialog;
    }
}
