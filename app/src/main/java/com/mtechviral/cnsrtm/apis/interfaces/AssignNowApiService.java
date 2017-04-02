package com.mtechviral.cnsrtm.apis.interfaces;

import com.mtechviral.cnsrtm.model.AssignNowRequest;
import com.mtechviral.cnsrtm.model.AssignNowResponse;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by pawankumar on 02/04/17.
 */

public interface AssignNowApiService {
    @POST("cnsadmin/assign/")
    Observable<AssignNowResponse> savePost(@Body AssignNowRequest assignNowRequest);
}
