package com.mtechviral.cnsrtm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by pawankumar on 01/04/17.
 */

public class CreateServiceRequest {
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("sno")
    @Expose
    private String serialNumber;
    @SerializedName("service_status")
    @Expose
    private String serviceStatus;
    @SerializedName("puchased_date")
    @Expose
    private String puchasedDate;
    @SerializedName("warranty")
    @Expose
    private String warranty;
    @SerializedName("spare_id")
    @Expose
    private String spareId;
    @SerializedName("service_days")
    @Expose
    private Integer serviceDays;

    public CreateServiceRequest(String token, String serial_number, Integer serviceDays,String spareID, String warranty, String purchaseDate, String status) {
        this.token = token;
        this.serialNumber = serial_number;
        this.spareId = spareID;
        this.warranty =  warranty;
        this.puchasedDate = purchaseDate;
        this.serviceStatus = status;
        this.serviceDays = serviceDays;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getServiceStatus() {
        return serviceStatus;
    }

    public void setServiceStatus(String serviceStatus) {
        this.serviceStatus = serviceStatus;
    }

    public String getPuchasedDate() {
        return puchasedDate;
    }

    public void setPuchasedDate(String puchasedDate) {
        this.puchasedDate = puchasedDate;
    }

    public String getWarranty() {
        return warranty;
    }

    public void setWarranty(String warranty) {
        this.warranty = warranty;
    }

    public String getSpareId() {
        return spareId;
    }

    public void setSpareId(String spareId) {
        this.spareId = spareId;
    }

    public Integer getServiceDays() {
        return serviceDays;
    }

    public void setServiceDays(Integer serviceDays) {
        this.serviceDays = serviceDays;
    }
}
