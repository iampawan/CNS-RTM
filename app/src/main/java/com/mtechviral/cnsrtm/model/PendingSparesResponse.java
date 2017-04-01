package com.mtechviral.cnsrtm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mtechviral.cnsrtm.model.datamodel.PendingSparesData;

import java.util.List;

/**
 * Created by pawankumar on 01/04/17.
 */

public class PendingSparesResponse {
    @SerializedName("data")
    @Expose
    private List<PendingSparesData> data = null;
    @SerializedName("message")
    @Expose
    private String message;

    public List<PendingSparesData> getData() {
        return data;
    }

    public void setData(List<PendingSparesData> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
