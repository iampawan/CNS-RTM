package com.mtechviral.cnsrtm.apis.interfaces;

import com.mtechviral.cnsrtm.model.EquipmentRequest;
import com.mtechviral.cnsrtm.model.EquipmentResponse;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by pawankumar on 28/03/17.
 */

public interface MaterialListApiService {
    @POST("material/")
    Observable<EquipmentResponse> savePost(@Body EquipmentRequest equipmentRequest);
}
