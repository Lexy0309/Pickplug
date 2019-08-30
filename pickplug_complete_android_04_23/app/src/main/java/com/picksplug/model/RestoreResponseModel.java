package com.picksplug.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by archive_infotech on 2/7/18.
 */

public class RestoreResponseModel {
    @SerializedName("response")
    @Expose
    private String response;
    @SerializedName("Message")
    @Expose
    private String message;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
