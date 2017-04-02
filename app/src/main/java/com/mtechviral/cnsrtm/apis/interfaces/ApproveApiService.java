package com.mtechviral.cnsrtm.apis.interfaces;

import com.mtechviral.cnsrtm.model.ApproveRequest;
import com.mtechviral.cnsrtm.model.ApproveResponse;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by pawankumar on 02/04/17.
 */

public interface ApproveApiService {
    @POST("cnsadmin/pending/")
    Observable<ApproveResponse> savePost(@Body ApproveRequest approveRequest);
    @POST("cnsadmin/approved/")
    Observable<ApproveResponse> changePost(@Body ApproveRequest approveRequest);
}
