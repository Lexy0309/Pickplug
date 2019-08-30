package com.picksplug.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by archive_infotech on 29/6/18.
 */

public class SubscriptionModel implements Serializable{
    @SerializedName("IsBestValue")
    @Expose
    private String isBestValue;
    @SerializedName("Id")
    @Expose
    private String id;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("Duration")
    @Expose
    private String duration;
    @SerializedName("Sport")
    @Expose
    private String sport;
    @SerializedName("StartDate")
    @Expose
    private String startDate;
    @SerializedName("EndDate")
    @Expose
    private String endDate;
    @SerializedName("Enabled")
    @Expose
    private String enabled;
    @SerializedName("ProductId")
    @Expose
    private String productId;

    public SubscriptionModel(String isBestValue, String id, String name, String duration, String sport, String startDate, String endDate, String enabled, String productId, String isPurchased) {
        this.isBestValue = isBestValue;
        this.id = id;
        this.name = name;
        this.duration = duration;
        this.sport = sport;
        this.startDate = startDate;
        this.endDate = endDate;
        this.enabled = enabled;
        this.productId = productId;
        this.isPurchased = isPurchased;
    }

    private String isPurchased;

    public String getIsBestValue() {
        return isBestValue;
    }

    public void setIsBestValue(String isBestValue) {
        this.isBestValue = isBestValue;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getEnabled() {
        return enabled;
    }

    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getIsPurchased() {
        return isPurchased;
    }

    public void setIsPurchased(String isPurchased) {
        this.isPurchased = isPurchased;
    }
}
