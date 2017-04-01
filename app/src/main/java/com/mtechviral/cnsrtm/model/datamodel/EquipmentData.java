package com.mtechviral.cnsrtm.model.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by pawankumar on 28/03/17.
 */

public class EquipmentData {
    @SerializedName("MaterialName")
    @Expose
    private String materialName;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("imageurl")
    @Expose
    private String imageUrl;

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


}
