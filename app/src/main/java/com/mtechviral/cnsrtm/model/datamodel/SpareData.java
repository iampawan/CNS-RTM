package com.mtechviral.cnsrtm.model.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by pawankumar on 30/03/17.
 */

public class SpareData {
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("sourced_from")
    @Expose
    private String sourcedFrom;
    @SerializedName("imageurl")
    @Expose
    private String imageurl;
    @SerializedName("factory_lead_time")
    @Expose
    private Integer factoryLeadTime;
    @SerializedName("warehouse")
    @Expose
    private String warehouse;
    @SerializedName("testing_required")
    @Expose
    private Boolean testingRequired;
    @SerializedName("shipping_time")
    @Expose
    private Integer shippingTime;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("quantity")
    @Expose
    private Integer quantity;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSourcedFrom() {
        return sourcedFrom;
    }

    public void setSourcedFrom(String sourcedFrom) {
        this.sourcedFrom = sourcedFrom;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public Integer getFactoryLeadTime() {
        return factoryLeadTime;
    }

    public void setFactoryLeadTime(Integer factoryLeadTime) {
        this.factoryLeadTime = factoryLeadTime;
    }

    public String getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }

    public Boolean getTestingRequired() {
        return testingRequired;
    }

    public void setTestingRequired(Boolean testingRequired) {
        this.testingRequired = testingRequired;
    }

    public Integer getShippingTime() {
        return shippingTime;
    }

    public void setShippingTime(Integer shippingTime) {
        this.shippingTime = shippingTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
