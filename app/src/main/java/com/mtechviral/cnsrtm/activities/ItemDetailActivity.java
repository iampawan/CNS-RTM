package com.mtechviral.cnsrtm.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.bumptech.glide.Glide;
import com.mtechviral.cnsrtm.R;
import com.mtechviral.cnsrtm.apis.ApiUtils;
import com.mtechviral.cnsrtm.apis.interfaces.SpareRequestApiService;
import com.mtechviral.cnsrtm.model.SpareOrderRequest;
import com.mtechviral.cnsrtm.model.SpareOrderResponse;
import com.mtechviral.cnsrtm.utils.Prefs;
import com.mtechviral.cnsrtm.utils.Utility;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ItemDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView numItem, tvSpareName, tvSpareQuantity, tvSpareShipping, tvSpareFactory,
            tvSpareTesting, tvSpareDescription, tvSpareWareHouse, tvSpareSourced;
    String spare_id, spare_name, spare_quantity, spare_description, spare_shipping_time,
            spare_factory_lead_time, spare_warehouse, spare_sourced_from, spare_img;
    Boolean spare_testing_required;
    ImageView imgSpare;
    SpareRequestApiService spareRequestApiService;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);
        spare_id = getIntent().getExtras().getString("spare_id");
        spare_name = getIntent().getExtras().getString("spare_name");
        spare_quantity = getIntent().getExtras().getString("spare_quantity");
        spare_description = getIntent().getExtras().getString("spare_description");
        spare_testing_required = getIntent().getExtras().getBoolean("spare_testing_required");
        spare_shipping_time = getIntent().getExtras().getString("spare_factory_lead_time");
        spare_factory_lead_time = getIntent().getExtras().getString("spare_factory_lead_time");
        spare_warehouse = getIntent().getExtras().getString("spare_warehouse");
        spare_sourced_from = getIntent().getExtras().getString("spare_sourced_from");
        spare_img = getIntent().getExtras().getString("spare_img");

        initToolbaritems(spare_name);
        initComponents();
    }

    private void initToolbaritems(String spare_name) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(spare_name);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initComponents() {
        numItem = (TextView) findViewById(R.id.numItem);
        tvSpareName = (TextView) findViewById(R.id.nameSpare);
        tvSpareQuantity = (TextView) findViewById(R.id.quantitySpare);
        tvSpareShipping = (TextView) findViewById(R.id.tvShipping);
        tvSpareDescription = (TextView) findViewById(R.id.tvDescription);
        tvSpareFactory = (TextView) findViewById(R.id.tvFactory);
        tvSpareSourced = (TextView) findViewById(R.id.tvSource);
        tvSpareTesting = (TextView) findViewById(R.id.tvTesting);
        tvSpareWareHouse = (TextView) findViewById(R.id.tvWarehouse);
        imgSpare = (ImageView) findViewById(R.id.imgSpare);
        spareRequestApiService = ApiUtils.getSpareRequestAPIService();
        setComponents();

    }

    private void setComponents() {
        tvSpareName.setText(spare_name);
        tvSpareDescription.setText(spare_description);
        tvSpareQuantity.setText("Available Quantity: " + spare_quantity);
        tvSpareShipping.setText("Shipping Time: \n" + spare_shipping_time + " days");
        tvSpareWareHouse.setText("Warehouse: \n" + spare_warehouse);
        tvSpareFactory.setText("Factory Lead Time: \n" + spare_factory_lead_time + " days");
        tvSpareSourced.setText("Sourced From: \n" + spare_sourced_from);
        if (spare_testing_required) {
            tvSpareTesting.setText("Testing Required: Yes");
        } else {
            tvSpareTesting.setText("Testing Required: No");
        }

        String td = spare_name.substring(0, 2);
        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
// generate random color
        int color1 = generator.getRandomColor();
        try {
            if (spare_img.equals("1")) {
                TextDrawable drawable = TextDrawable.builder()
                        .buildRect(td, color1);

                imgSpare.setScaleType(ImageView.ScaleType.FIT_CENTER);
                imgSpare.setImageDrawable(drawable);

            } else {
                Glide.with(this)
//                .load("https://s3-us-west-2.amazonaws.com/material-ui-template/ecommerce/style-6/Ecommerce-6-img-1.jpg")
                        .load(spare_img)
                        .thumbnail(0.01f)
                        .fitCenter()
                        .placeholder(R.drawable.loading_placeholder)
                        .into(imgSpare);
            }
        } catch (Exception e) {
        }


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.requestButton:
//                Toast.makeText(this, "Button Apply Changes clicked!", Toast.LENGTH_SHORT).show();
                requestSpare();
                break;
            case R.id.buttonMinus:
                int num = Integer.parseInt(numItem.getText().toString());
                if (num > 1) {
                    num--;
                }
                numItem.setText(Integer.toString(num));
                break;
            case R.id.buttonPlus:
                int num2 = Integer.parseInt(numItem.getText().toString());
                num2++;
                numItem.setText(Integer.toString(num2));
                break;

        }
    }

    private void requestSpare() {
        final SpareOrderRequest spareOrderRequest = new SpareOrderRequest(Prefs.getString("token", ""), spare_id);
        pd = Utility.showProgress(this, R.string.please_wait);
        spareRequestApiService.savePost(spareOrderRequest).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SpareOrderResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        pd.dismiss();
                        onFailRequest(e.getMessage());

                    }

                    @Override
                    public void onNext(SpareOrderResponse spareOrderResponse) {
                        if (spareOrderResponse.getMessage().equals("Success")) {
                            displayApiResult(spareOrderResponse);
                        } else {
                            onFailRequest(getString(R.string.something_wrong));
                        }
                    }

                });
    }

    private void displayApiResult(SpareOrderResponse spareOrderResponse) {
        pd.dismiss();
        new AlertDialog.Builder(this)
                .setTitle("Success")
                .setMessage("Request is accepted. It will be processed shortly.")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent i = new Intent(ItemDetailActivity.this, HomeActivity.class);
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

}
