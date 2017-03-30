package com.mtechviral.cnsrtm.apis.interfaces;

import com.mtechviral.cnsrtm.model.SpareRequest;
import com.mtechviral.cnsrtm.model.SpareResponse;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by pawankumar on 30/03/17.
 */

public interface SpareListApiService {
    @POST("materialdetail/")
    Observable<SpareResponse> savePost(@Body SpareRequest spareRequest);
}
