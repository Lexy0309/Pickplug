package com.picksplug.model;

import com.android.billingclient.api.SkuDetails;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class InappPurchaseModel {
    @SerializedName("isSubscription")
    @Expose
    public Boolean isSubscription;
    @SerializedName("Name")
    @Expose
    public String name;
    @SerializedName("Price")
    @Expose
    public String price;
    @SerializedName("Duration")
    @Expose
    public String duration;
    @SerializedName("Description")
    @Expose
    public String description;
    @SerializedName("StartDate")
    @Expose
    public String startDate;
    @SerializedName("EndDate")
    @Expose
    public String endDate;
    @SerializedName("ProductId")
    @Expose
    public String productId;
    @SerializedName("isPurchased")
    @Expose
    public Boolean isPurchased;
    @SerializedName("SkuDetails")
    @Expose
    public SkuDetails skuDetails;

    public InappPurchaseModel(Boolean isSubscription, String name, String price, String duration, String description, String startDate, String endDate, String productId, Boolean isPurchased, SkuDetails skuDetails) {
        this.isSubscription = isSubscription;
        this.name = name;
        this.price = price;
        this.duration = duration;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.productId = productId;
        this.isPurchased = isPurchased;
        this.skuDetails = skuDetails;
    }
}
