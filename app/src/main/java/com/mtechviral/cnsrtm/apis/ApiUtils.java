package com.mtechviral.cnsrtm.apis;

import com.mtechviral.cnsrtm.apis.interfaces.CreateServiceApiService;
import com.mtechviral.cnsrtm.apis.interfaces.LoginApiService;
import com.mtechviral.cnsrtm.apis.interfaces.MaterialListApiService;
import com.mtechviral.cnsrtm.apis.interfaces.PendingSpareApiService;
import com.mtechviral.cnsrtm.apis.interfaces.SpareListApiService;
import com.mtechviral.cnsrtm.apis.interfaces.SpareRequestApiService;

/**
 * Created by pawankumar on 09/03/17.
 */

public class ApiUtils {
    private ApiUtils() {}

    private static final String BASE_URL = "http://192.168.104.244:8000/";

    public static LoginApiService getLoginAPIService() {

        return RetrofitClient.getClient(BASE_URL).create(LoginApiService.class);
    }

    public static MaterialListApiService getMaterialListAPIService() {

        return RetrofitClient.getClient(BASE_URL).create(MaterialListApiService.class);
    }

    public static SpareListApiService getSpareListAPIService() {

        return RetrofitClient.getClient(BASE_URL).create(SpareListApiService.class);
    }
    public static SpareRequestApiService getSpareRequestAPIService() {

        return RetrofitClient.getClient(BASE_URL).create(SpareRequestApiService.class);
    }
    public static PendingSpareApiService getPendingSpareAPIService() {

        return RetrofitClient.getClient(BASE_URL).create(PendingSpareApiService.class);
    }
    public static CreateServiceApiService getCreateServiceAPIService() {

        return RetrofitClient.getClient(BASE_URL).create(CreateServiceApiService.class);
    }

}
