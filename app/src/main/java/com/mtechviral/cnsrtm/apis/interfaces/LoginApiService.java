package com.mtechviral.cnsrtm.apis.interfaces;

import com.mtechviral.cnsrtm.model.LoginRequest;
import com.mtechviral.cnsrtm.model.LoginResponse;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by pawankumar on 09/03/17.
 */

public interface LoginApiService {
    @POST("login/")
    Observable<LoginResponse> savePost(@Body LoginRequest loginPost);
    @POST("cnsadmin/login/")
    Observable<LoginResponse> saveAdminPost(@Body LoginRequest loginPost);

}
