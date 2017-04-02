package com.mtechviral.cnsrtm.model.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by pawankumar on 02/04/17.
 */

public class ApproveData {
    @SerializedName("approved")
    @Expose
    private Boolean approved;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("eol")
    @Expose
    private String eol;
    @SerializedName("stage")
    @Expose
    private String stage;
    @SerializedName("serialnum")
    @Expose
    private String serialnum;
    @SerializedName("sparename")
    @Expose
    private String sparename;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("materialname")
    @Expose
    private String materialname;

    public Boolean getApproved() {
        return approved;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getEol() {
        return eol;
    }

    public void setEol(String eol) {
        this.eol = eol;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public String getSerialnum() {
        return serialnum;
    }

    public void setSerialnum(String serialnum) {
        this.serialnum = serialnum;
    }

    public String getSparename() {
        return sparename;
    }

    public void setSparename(String sparename) {
        this.sparename = sparename;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMaterialname() {
        return materialname;
    }

    public void setMaterialname(String materialname) {
        this.materialname = materialname;
    }

}
