package com.mtechviral.cnsrtm.apis.interfaces;

import com.mtechviral.cnsrtm.model.PendingSparesRequest;
import com.mtechviral.cnsrtm.model.PendingSparesResponse;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by pawankumar on 01/04/17.
 */

public interface PendingSpareApiService {
    @POST("request_detail/")
    Observable<PendingSparesResponse> savePost(@Body PendingSparesRequest pendingSparesRequest);
}
