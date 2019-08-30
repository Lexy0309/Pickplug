package com.picksplug.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by archive_infotech on 26/6/18.
 */

public class PicksModel {
    @SerializedName("SportDetails")
    @Expose
    private List<SportsDetailModel> sportDetails = null;
    @SerializedName("PickDetails")
    @Expose
    private List<PickDetailModel> pickDetails = null;

    public List<SportsDetailModel> getSportDetails() {
        return sportDetails;
    }

    public void setSportDetails(List<SportsDetailModel> sportDetails) {
        this.sportDetails = sportDetails;
    }

    public List<PickDetailModel> getPickDetails() {
        return pickDetails;
    }

    public void setPickDetails(List<PickDetailModel> pickDetails) {
        this.pickDetails = pickDetails;
    }
}
