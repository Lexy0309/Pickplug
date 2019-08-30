package com.picksplug.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by archive_infotech on 7/10/18.
 */

public class PickBySportResultModel {
    @SerializedName("is_allow")
    @Expose
    private String isAllow;
    @SerializedName("allpicks")
    @Expose
    private AllPicksModel allpicks;

    public String getIsAllow() {
        return isAllow;
    }

    public void setIsAllow(String isAllow) {
        this.isAllow = isAllow;
    }

    public AllPicksModel getAllpicks() {
        return allpicks;
    }

    public void setAllpicks(AllPicksModel allpicks) {
        this.allpicks = allpicks;
    }

}
