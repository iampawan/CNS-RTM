package com.mtechviral.cnsrtm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by pawankumar on 01/04/17.
 */

public class QrRequest {
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("sno")
    @Expose
    private String sno;
    @SerializedName("req_type")
    @Expose
    private Integer reqType;

    public QrRequest(String token, String sno, Integer reqType){
        this.token = token;
        this.sno = sno;
        this.reqType = reqType;
    }

    public Integer getReqType() {
        return reqType;
    }

    public void setReqType(Integer reqType) {
        this.reqType = reqType;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }
}
