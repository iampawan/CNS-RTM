package com.mtechviral.cnsrtm.apis.interfaces;

import com.mtechviral.cnsrtm.model.CreateServiceRequest;
import com.mtechviral.cnsrtm.model.CreateServiceResponse;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by pawankumar on 01/04/17.
 */

public interface CreateServiceApiService {
    @POST("service_spare/")
    Observable<CreateServiceResponse> savePost(@Body CreateServiceRequest createServiceRequest);
}
