
package com.picksplug.CommonBean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JsonObjectResponse<T> {

    @SerializedName("RESULT")
    @Expose
    public String rESULT;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("Data")
    public T object;

    public String getRESULT() {
        return rESULT;
    }

    public void setRESULT(String rESULT) {
        this.rESULT = rESULT;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getObject() {
        return object;
    }

    public void setObject(T object) {
        this.object = object;
    }
}
