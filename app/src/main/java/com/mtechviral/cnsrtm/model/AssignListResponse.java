package com.mtechviral.cnsrtm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by pawankumar on 02/04/17.
 */

public class AssignListResponse {
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<AssignMaterial> material = null;


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<AssignMaterial> getAssignMaterial() {
        return material;
    }

    public void setAssignMaterial(List<AssignMaterial> material) {
        this.material = material;
    }


}
