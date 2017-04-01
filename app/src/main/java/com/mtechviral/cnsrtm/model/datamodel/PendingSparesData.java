package com.mtechviral.cnsrtm.model.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by pawankumar on 01/04/17.
 */

public class PendingSparesData {
    @SerializedName("assigned_to")
    @Expose
    private Object assignedTo;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("sparename")
    @Expose
    private String sparename;
    @SerializedName("created")
    @Expose
    private String created;

    public Object getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(Object assignedTo) {
        this.assignedTo = assignedTo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSparename() {
        return sparename;
    }

    public void setSparename(String sparename) {
        this.sparename = sparename;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

}
