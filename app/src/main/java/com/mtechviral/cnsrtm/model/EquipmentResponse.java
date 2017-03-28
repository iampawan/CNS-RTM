package com.mtechviral.cnsrtm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mtechviral.cnsrtm.model.datamodel.EquipmentData;

import java.util.ArrayList;

/**
 * Created by pawankumar on 28/03/17.
 */

public class EquipmentResponse {
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private ArrayList<EquipmentData> data = null;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<EquipmentData> getData() {
        return data;
    }

    public void setData(ArrayList<EquipmentData> data) {
        this.data = data;
    }

}
