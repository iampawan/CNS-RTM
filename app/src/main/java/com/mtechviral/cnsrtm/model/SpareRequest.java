package com.mtechviral.cnsrtm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by pawankumar on 30/03/17.
 */

public class SpareRequest {
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("mat_id")
    @Expose
    private Integer matId;


    public SpareRequest(String token, Integer matid){
        this.token = token;
        this.matId = matid;
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getMatId() {
        return matId;
    }

    public void setMatId(Integer matId) {
        this.matId = matId;
    }
}
