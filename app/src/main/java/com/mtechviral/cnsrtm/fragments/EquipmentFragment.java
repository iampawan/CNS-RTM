package com.mtechviral.cnsrtm.fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

/**
 * Created by pawankumar on 28/03/17.
 */

@SuppressLint("ValidFragment")
public class EquipmentFragment extends Fragment implements EquipmentClickListener {
    int wizard_page_position;
    MaterialListApiService materialListApiService;

    public EquipmentFragment(int position) {
        this.wizard_page_position = position;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int layout_id = R.layout.equipment_fragment;
        View view = inflater.inflate(layout_id, container, false);

        materialListApiService = ApiUtils.getMaterialListAPIService();


        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        RecyclerView rView = (RecyclerView) view.findViewById(R.id.recyclerView);
        rView.setHasFixedSize(true);
        rView.setLayoutManager(layoutManager);
        rView.setNestedScrollingEnabled(false);
        rView.setHasFixedSize(false);

        ArrayList<EquipmentData> rowListItem = loadJSON();

        EquipmentAdapter rcAdapter = new EquipmentAdapter(getActivity(), rowListItem);
        rView.setAdapter(rcAdapter);
        rcAdapter.setClickListener(this);
        return view;
    }

    private ArrayList<EquipmentData> loadJSON(){
        final ProgressDialog pd = Utility.showProgress(getActivity(),R.string.please_wait);
        final EquipmentRequest equipmentRequest = new EquipmentRequest(Prefs.getString("token",""));
        final ArrayList<EquipmentData> allItems = new ArrayList<>();

        materialListApiService.savePost(equipmentRequest).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<EquipmentResponse>() {
                    @Override
                    public void onCompleted() {
                        pd.dismiss();

                    }

                    @Override
                    public void onError(Throwable e) {
                        pd.dismiss();
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onNext(EquipmentResponse equipmentResponse) {
                        pd.dismiss();
                        int datasize = equipmentResponse.getData().size();
                        for (int i = 0; i<datasize;i++){

                            EquipmentData dt ;
                           allItems.add(i,equipmentResponse.getData().get(i));
                        }




                    }

                });

        return  allItems;
    }

    @Override
    public void itemClicked(View view, int position) {
        int num = position + 1;
        Toast.makeText(getActivity(), "Position " + num + " clicked!", Toast.LENGTH_SHORT).show();
    }
}