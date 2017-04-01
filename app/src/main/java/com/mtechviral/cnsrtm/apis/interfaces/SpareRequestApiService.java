package com.mtechviral.cnsrtm.apis.interfaces;

import com.mtechviral.cnsrtm.model.SpareOrderRequest;
import com.mtechviral.cnsrtm.model.SpareOrderResponse;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by pawankumar on 01/04/17.
 */

public interface SpareRequestApiService {
    @POST("request_create/")
    Observable<SpareOrderResponse> savePost(@Body SpareOrderRequest spareOrderRequest);
}
