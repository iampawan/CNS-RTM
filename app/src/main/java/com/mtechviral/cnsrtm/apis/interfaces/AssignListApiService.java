package com.mtechviral.cnsrtm.apis.interfaces;

import com.mtechviral.cnsrtm.model.AssignListRequest;
import com.mtechviral.cnsrtm.model.AssignListResponse;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by pawankumar on 02/04/17.
 */

public interface AssignListApiService {
    @POST("cnsadmin/assign_list/")
    Observable<AssignListResponse> savePost(@Body AssignListRequest assignListRequest);
}
