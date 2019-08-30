package com.picksplug.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by archive_infotech on 7/11/18.
 */

public class UserSubscriptionModel {
    @SerializedName("Id")
    @Expose
    private String id;
    @SerializedName("User")
    @Expose
    private String user;
    @SerializedName("Sport")
    @Expose
    private String sport;
    @SerializedName("ExpiryDate")
    @Expose
    private String expiryDate;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;
    @SerializedName("expiry_duration")
    @Expose
    private String expiryDuration;
    @SerializedName("is_new_app_subscription")
    @Expose
    private String isNewAppSubscription;
    @SerializedName("subscription_id")
    @Expose
    private String subscriptionId;
    @SerializedName("IsBestValue")
    @Expose
    private String isBestValue;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("Duration")
    @Expose
    private String duration;
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
    @SerializedName("SportDetail")
    @Expose
    private List<SportDetail> sportDetail = null;
    @SerializedName("sportIconImgPrefix")
    @Expose
    private String sportIconImgPrefix;

    public UserSubscriptionModel(String id, String user, String sport, String expiryDate, String updatedAt, String expiryDuration, String isNewAppSubscription, String subscriptionId, String isBestValue, String name, String duration, String startDate, String endDate, String enabled, String productId, List<SportDetail> sportDetail, String sportIconImgPrefix) {
        this.id = id;
        this.user = user;
        this.sport = sport;
        this.expiryDate = expiryDate;
        this.updatedAt = updatedAt;
        this.expiryDuration = expiryDuration;
        this.isNewAppSubscription = isNewAppSubscription;
        this.subscriptionId = subscriptionId;
        this.isBestValue = isBestValue;
        this.name = name;
        this.duration = duration;
        this.startDate = startDate;
        this.endDate = endDate;
        this.enabled = enabled;
        this.productId = productId;
        this.sportDetail = sportDetail;
        this.sportIconImgPrefix = sportIconImgPrefix;
    }

    public UserSubscriptionModel(String id, String user, String sport, String expiryDate, String updatedAt, String expiryDuration, String isNewAppSubscription, String subscriptionId) {
        this.id = id;
        this.user = user;
        this.sport = sport;
        this.expiryDate = expiryDate;
        this.updatedAt = updatedAt;
        this.expiryDuration = expiryDuration;
        this.isNewAppSubscription = isNewAppSubscription;
        this.subscriptionId = subscriptionId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getExpiryDuration() {
        return expiryDuration;
    }

    public void setExpiryDuration(String expiryDuration) {
        this.expiryDuration = expiryDuration;
    }

    public String getIsNewAppSubscription() {
        return isNewAppSubscription;
    }

    public void setIsNewAppSubscription(String isNewAppSubscription) {
        this.isNewAppSubscription = isNewAppSubscription;
    }

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(String subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public String getIsBestValue() {
        return isBestValue;
    }

    public void setIsBestValue(String isBestValue) {
        this.isBestValue = isBestValue;
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

    public List<SportDetail> getSportDetail() {
        return sportDetail;
    }

    public void setSportDetail(List<SportDetail> sportDetail) {
        this.sportDetail = sportDetail;
    }

    public String getSportIconImgPrefix() {
        return sportIconImgPrefix;
    }

    public void setSportIconImgPrefix(String sportIconImgPrefix) {
        this.sportIconImgPrefix = sportIconImgPrefix;
    }

    public static class SportDetail {

        @SerializedName("Id")
        @Expose
        private String id;
        @SerializedName("SportName")
        @Expose
        private String sportName;
        @SerializedName("Ranking")
        @Expose
        private String ranking;
        @SerializedName("SportIcon")
        @Expose
        private String sportIcon;
        @SerializedName("thumb")
        @Expose
        private String thumb;
        @SerializedName("AddedDate")
        @Expose
        private String addedDate;
        @SerializedName("ModifiedDate")
        @Expose
        private String modifiedDate;
        @SerializedName("Status")
        @Expose
        private String status;
        @SerializedName("RecordNo")
        @Expose
        private String recordNo;
        @SerializedName("IsDeleted")
        @Expose
        private String isDeleted;

        public SportDetail(String id, String sportName, String ranking, String sportIcon, String thumb, String addedDate, String modifiedDate, String status, String recordNo, String isDeleted) {
            this.id = id;
            this.sportName = sportName;
            this.ranking = ranking;
            this.sportIcon = sportIcon;
            this.thumb = thumb;
            this.addedDate = addedDate;
            this.modifiedDate = modifiedDate;
            this.status = status;
            this.recordNo = recordNo;
            this.isDeleted = isDeleted;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSportName() {
            return sportName;
        }

        public void setSportName(String sportName) {
            this.sportName = sportName;
        }

        public String getRanking() {
            return ranking;
        }

        public void setRanking(String ranking) {
            this.ranking = ranking;
        }

        public String getSportIcon() {
            return sportIcon;
        }

        public void setSportIcon(String sportIcon) {
            this.sportIcon = sportIcon;
        }

        public String getThumb() {
            return thumb;
        }

        public void setThumb(String thumb) {
            this.thumb = thumb;
        }

        public String getAddedDate() {
            return addedDate;
        }

        public void setAddedDate(String addedDate) {
            this.addedDate = addedDate;
        }

        public String getModifiedDate() {
            return modifiedDate;
        }

        public void setModifiedDate(String modifiedDate) {
            this.modifiedDate = modifiedDate;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getRecordNo() {
            return recordNo;
        }

        public void setRecordNo(String recordNo) {
            this.recordNo = recordNo;
        }

        public String getIsDeleted() {
            return isDeleted;
        }

        public void setIsDeleted(String isDeleted) {
            this.isDeleted = isDeleted;
        }
    }
}
