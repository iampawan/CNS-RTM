package com.mtechviral.cnsrtm.apis;

import com.mtechviral.cnsrtm.apis.interfaces.ApproveApiService;
import com.mtechviral.cnsrtm.apis.interfaces.AssignListApiService;
import com.mtechviral.cnsrtm.apis.interfaces.AssignNowApiService;
import com.mtechviral.cnsrtm.apis.interfaces.CreateServiceApiService;
import com.mtechviral.cnsrtm.apis.interfaces.LoginApiService;
import com.mtechviral.cnsrtm.apis.interfaces.MaterialCreateApiService;
import com.mtechviral.cnsrtm.apis.interfaces.MaterialListApiService;
import com.mtechviral.cnsrtm.apis.interfaces.PendingSpareApiService;
import com.mtechviral.cnsrtm.apis.interfaces.QRApiService;
import com.mtechviral.cnsrtm.apis.interfaces.SpareListApiService;
import com.mtechviral.cnsrtm.apis.interfaces.SpareRequestApiService;

/**
 * Created by pawankumar on 09/03/17.
 */

public class ApiUtils {
    private ApiUtils() {}

    private static final String BASE_URL = "http://192.168.104.244:8000/";
//    private static final String BASE_URL = "http://172.20.10.3:8000/";
    private static final String BASE_URL_ADMIN = "http://192.168.104.244:8000/cnsadmin/";
//    private static final String BASE_URL_ADMIN = "http://172.20.10.3:8000/cnsadmin/";

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
    public static MaterialCreateApiService getMaterialCreateAPIService() {

        return RetrofitClient.getClient(BASE_URL).create(MaterialCreateApiService.class);
    }
    public static QRApiService getQRAPIService() {

        return RetrofitClient.getClient(BASE_URL).create(QRApiService.class);
    }
    public static ApproveApiService getAprooveAPIService() {

        return RetrofitClient.getClient(BASE_URL).create(ApproveApiService.class);
    }
    public static AssignListApiService getAssignListAPIService() {

        return RetrofitClient.getClient(BASE_URL).create(AssignListApiService.class);
    }
    public static AssignNowApiService getAssignNowAPIService() {

        return RetrofitClient.getClient(BASE_URL).create(AssignNowApiService.class);
    }

}
