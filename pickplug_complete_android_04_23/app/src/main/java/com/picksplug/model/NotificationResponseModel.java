package com.picksplug.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by archive_infotech on 3/7/18.
 */

public class NotificationResponseModel {
    @SerializedName("Results")
    @Expose
    private List<Result> results = null;

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

    public static class Result {

        @SerializedName("notification_id")
        @Expose
        private String notificationId;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("created_at")
        @Expose
        private String createdAt;

        public Result(String notificationId, String title, String message, String createdAt) {
            this.notificationId = notificationId;
            this.title = title;
            this.message = message;
            this.createdAt = createdAt;
        }

        public String getNotificationId() {
            return notificationId;
        }

        public void setNotificationId(String notificationId) {
            this.notificationId = notificationId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }
    }
}
