package com.mtechviral.cnsrtm.activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.mtechviral.cnsrtm.R;
import com.mtechviral.cnsrtm.apis.ApiUtils;
import com.mtechviral.cnsrtm.apis.interfaces.CreateServiceApiService;
import com.mtechviral.cnsrtm.model.CreateServiceRequest;
import com.mtechviral.cnsrtm.model.CreateServiceResponse;
import com.mtechviral.cnsrtm.utils.Prefs;
import com.mtechviral.cnsrtm.utils.Utility;

import java.util.Calendar;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CreateServiceActivity extends AppCompatActivity implements View.OnClickListener{

    EditText etSerial,etSpareID,etWarranty,etPurchaseDate,etInspection,etServiceStatus;
    Button btnDone;
    CreateServiceApiService createServiceApiService;
    ProgressDialog pd;
    String serial_number,spareID,warranty,purchaseDate,status;
    Integer inspection;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private Calendar c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_service);
        initToolbarItems();
        initComponents();

    }

    private void initToolbarItems(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.create_service);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }
    private void initComponents(){
        createServiceApiService = ApiUtils.getCreateServiceAPIService();
        etSerial = (EditText) findViewById(R.id.etSerial);
        etSpareID = (EditText) findViewById(R.id.etSpareID);
        etWarranty = (EditText) findViewById(R.id.etWarranty);
        etPurchaseDate = (EditText) findViewById(R.id.etPurchaseDate);
        etInspection = (EditText) findViewById(R.id.etInspection);
        etServiceStatus = (EditText) findViewById(R.id.etStatus);
        btnDone = (Button) findViewById(R.id.btnDone);

    }
    private void setComponents(){
        serial_number = etSerial.getText().toString();
         spareID = etSpareID.getText().toString();
         warranty = etWarranty.getText().toString();
         inspection = Integer.valueOf(etInspection.getText().toString());
         purchaseDate = etPurchaseDate.getText().toString();
         status = etServiceStatus.getText().toString();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnDone:
                setComponents();
//                Toast.makeText(this, "Button Apply Changes clicked!", Toast.LENGTH_SHORT).show();
                if (!TextUtils.isEmpty(serial_number) && !TextUtils.isEmpty(spareID)
                        && !TextUtils.isEmpty(warranty)&& !TextUtils.isEmpty(purchaseDate)
                        && !TextUtils.isEmpty(inspection.toString()) && !TextUtils.isEmpty(status)) {
                    if (Utility.isNetworkConnected(this)) {
                            createService();
                    } else {
                        Toast.makeText(this, R.string.check_internet, Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(this, R.string.enter_all_fields, Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.etPurchaseDate:
                c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);
                callDatePicker();
                break;


        }
    }

    private void createService() {
        final CreateServiceRequest createServiceRequest = new CreateServiceRequest(
                Prefs.getString("token",""),serial_number,inspection,spareID,warranty,purchaseDate, status);
        pd = Utility.showProgress(this, R.string.please_wait);
        createServiceApiService.savePost(createServiceRequest).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CreateServiceResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        pd.dismiss();
                        onFailRequest(e.getMessage());

                    }

                    @Override
                    public void onNext(CreateServiceResponse createServiceResponse) {
                        if (createServiceResponse.getMessage().equals("Success")) {
                            displayApiResult(createServiceResponse);
                        } else {
                            onFailRequest(getString(R.string.something_wrong));
                        }
                    }

                });
    }

    private void displayApiResult(CreateServiceResponse createServiceResponse) {
        pd.dismiss();
        new AlertDialog.Builder(this)
                .setTitle("Success")
                .setMessage("Request is accepted. It will be processed shortly.")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent i = new Intent(CreateServiceActivity.this, HomeActivity.class);
                        startActivity(i);
                        finish();
                    }
                })
                .show();
//        Toast.makeText(this, spareOrderResponse.getMessage(), Toast.LENGTH_SHORT).show();
    }

    private void onFailRequest(String message) {
        pd.dismiss();
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void callDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

//                        txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        c.set(year, monthOfYear, dayOfMonth);
                        callTimePicker(dayOfMonth, monthOfYear, year);


                    }
                }, mYear, mMonth, mDay);
//        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    private void callTimePicker(final int dom, final int moy, final int y) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                            c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                            c.set(Calendar.MINUTE, minute);
                            c.set(Calendar.SECOND, 0);
                            etPurchaseDate.setText(y+"-"+(moy + 1)+"-"+dom+" "+hourOfDay+":"+minute);
                            purchaseDate = etPurchaseDate.getText().toString();
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();

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
