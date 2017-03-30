package com.mtechviral.cnsrtm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mtechviral.cnsrtm.model.datamodel.SpareData;

import java.util.List;

/**
 * Created by pawankumar on 30/03/17.
 */

public class SpareResponse {
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<SpareData> data = null;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<SpareData> getData() {
        return data;
    }

    public void setData(List<SpareData> data) {
        this.data = data;
    }
}
