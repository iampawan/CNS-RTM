package com.mtechviral.cnsrtm.apis;

import com.mtechviral.cnsrtm.apis.interfaces.LoginApiService;

/**
 * Created by pawankumar on 09/03/17.
 */

public class ApiUtils {
    private ApiUtils() {}

    public static final String BASE_URL = "http://192.168.1.89:8000/";

    public static LoginApiService getLoginAPIService() {

        return RetrofitClient.getClient(BASE_URL).create(LoginApiService.class);
    }
}