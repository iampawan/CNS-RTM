package com.mtechviral.cnsrtm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by pawankumar on 01/04/17.
 */

public class SpareOrderRequest {
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("spare_id")
    @Expose
    private String spareId;

    public SpareOrderRequest(String token, String spareId){
        this.token = token;
        this.spareId = spareId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSpareId() {
        return spareId;
    }

    public void setSpareId(String spareId) {
        this.spareId = spareId;
    }

}
