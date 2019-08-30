package com.picksplug.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by archive_infotech on 3/7/18.
 */

public class AddTokenResponseModel {
    @SerializedName("Results")
    @Expose
    private Results results;

    public Results getResults() {
        return results;
    }

    public void setResults(Results results) {
        this.results = results;
    }

    public class Results {

        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("message")
        @Expose
        private String message;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

    }
}
