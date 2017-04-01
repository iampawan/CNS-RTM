package com.mtechviral.cnsrtm.apis.interfaces;

import com.mtechviral.cnsrtm.model.QrRequest;
import com.mtechviral.cnsrtm.model.QrResponse;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by pawankumar on 01/04/17.
 */

public interface QRApiService {
    @POST("scan/")
    Observable<QrResponse> savePost(@Body QrRequest qrRequest);
}
