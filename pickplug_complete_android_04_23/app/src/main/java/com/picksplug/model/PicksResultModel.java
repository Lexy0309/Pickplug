package com.picksplug.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by archive_infotech on 7/10/18.
 */

public class PicksResultModel {
    @SerializedName("allpicks")
    @Expose
    private List<AllPicksModel> allpicks = null;
    @SerializedName("teamImgPrefix")
    @Expose
    private String teamImgPrefix;

    public PicksResultModel(AllPicksModel allPicksModel) {

    }

    public PicksResultModel(AllPicksModel[] allPicksModels) {

    }

    public List<AllPicksModel> getAllpicks() {
        return allpicks;
    }

    public void setAllpicks(List<AllPicksModel> allpicks) {
        this.allpicks = allpicks;
    }

    public String getTeamImgPrefix() {
        return teamImgPrefix;
    }

    public void setTeamImgPrefix(String teamImgPrefix) {
        this.teamImgPrefix = teamImgPrefix;
    }

}
