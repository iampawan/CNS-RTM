package com.mtechviral.cnsrtm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mtechviral.cnsrtm.model.datamodel.ApproveData;

import java.util.List;

/**
 * Created by pawankumar on 02/04/17.
 */

public class ApproveResponse {
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<ApproveData> data = null;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ApproveData> getData() {
        return data;
    }

    public void setData(List<ApproveData> data) {
        this.data = data;
    }

}
