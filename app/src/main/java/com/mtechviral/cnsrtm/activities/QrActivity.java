package com.mtechviral.cnsrtm.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.mtechviral.cnsrtm.R;
import com.mtechviral.cnsrtm.apis.ApiUtils;
import com.mtechviral.cnsrtm.apis.interfaces.QRApiService;
import com.mtechviral.cnsrtm.model.QrRequest;
import com.mtechviral.cnsrtm.model.QrResponse;
import com.mtechviral.cnsrtm.utils.Prefs;
import com.mtechviral.cnsrtm.utils.Utility;

import java.io.UnsupportedEncodingException;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class QrActivity extends AppCompatActivity implements QRCodeReaderView.OnQRCodeReadListener {

    private QRCodeReaderView qrCodeReaderView;
    QRApiService qrApiService;
    ProgressDialog pd;
    Boolean flag = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);
        initToolbarItems();
        initComponents();
    }

    private void initToolbarItems(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Scan QR");
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }

    private void initComponents(){
        qrApiService = ApiUtils.getQRAPIService();
        setComponents();
    }

    private void setComponents(){
        qrCodeReaderView = (QRCodeReaderView) findViewById(R.id.qrdecoderview);
        qrCodeReaderView.setOnQRCodeReadListener(this);
        qrCodeReaderView.setQRDecodingEnabled(true);
        qrCodeReaderView.setAutofocusInterval(2000L);
        qrCodeReaderView.setBackCamera();
    }


    @Override
    public void onQRCodeRead(String text, PointF[] points) {
        String qrText = text;
        String name = "", id="" ;
        byte[] tmp2 = Base64.decode(qrText,Base64.DEFAULT);
        try {
           qrText = new String(tmp2, "UTF-8");
            Log.d("qr", "onQRCodeRead: "+qrText);
            String[] parts = qrText.split("-");
            name = parts[0].trim();
            id = parts[1].trim();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
//        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
        if(flag){
            final String finalId = id;
            new MaterialDialog.Builder(QrActivity.this)
                    .title(name)
                    .items(R.array.item_detail_option)
                    .cancelable(false)
                    .itemsCallback(new MaterialDialog.ListCallback() {
                        @Override
                        public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                            dialog.dismiss();
                            Log.d("which2", "onSelection: " + which);
                            callQrOption(finalId, which + 1);
                        }
                    })
                    .show();
        }
        else {

        }
        flag = false;
    }

    private void callQrOption(String qrText,Integer req_type){
        pd = Utility.showProgress(this,R.string.please_wait);
        final QrRequest qrRequest = new QrRequest(Prefs.getString("token", ""),qrText,req_type);

        qrApiService.savePost(qrRequest).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<QrResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        flag = true;
                        onFailRequest(e.getMessage());

                    }

                    @Override
                    public void onNext(QrResponse qrResponse) {
                        flag = true;
                        if (qrResponse.getMessage().equals("Success")) {
                            displayApiResult(qrResponse);
                        } else {
                            onFailRequest(getString(R.string.something_wrong));
                        }
                    }

                });
    }

    private void displayApiResult(QrResponse qrResponse) {
        pd.dismiss();
        new AlertDialog.Builder(this)
                .setTitle("Success")
                .setMessage("Your request id is: "+qrResponse.getReqId()+". It will be processed shortly.")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
//                        Intent i = new Intent(QrActivity.this, HomeActivity.class);
//                        startActivity(i);
//                        finish();
                    }
                })
                .show();
    }

    private void onFailRequest(String message) {
        pd.dismiss();
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        qrCodeReaderView.startCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        qrCodeReaderView.stopCamera();
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
