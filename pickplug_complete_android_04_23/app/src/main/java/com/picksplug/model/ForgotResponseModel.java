package com.picksplug.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by LENOVO on 6/28/2018.
 */

public class ForgotResponseModel {
    @SerializedName("Forget")
    @Expose
    private Forget forget;

    public Forget getForget() {
        return forget;
    }

    public void setForget(Forget forget) {
        this.forget = forget;
    }


    public class Forget {

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
