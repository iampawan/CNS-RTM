package com.mtechviral.cnsrtm.apis.interfaces;

import com.mtechviral.cnsrtm.model.MaterialCreateRequest;
import com.mtechviral.cnsrtm.model.MaterialCreateResponse;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by pawankumar on 01/04/17.
 */

public interface MaterialCreateApiService {
    @POST("material_create/")
    Observable<MaterialCreateResponse> savePost(@Body MaterialCreateRequest materialCreateRequest);
}
