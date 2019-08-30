package com.picksplug.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by archive_infotech on 26/6/18.
 */

public class PicksResponseModel {
    @SerializedName("Picks")
    @Expose
    private PicksModel picks;

    public PicksModel getPicks() {
        return picks;
    }

    public void setPicks(PicksModel picks) {
        this.picks = picks;
    }
}
