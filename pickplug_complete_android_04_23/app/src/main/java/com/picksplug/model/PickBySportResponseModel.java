package com.picksplug.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by archive_infotech on 7/10/18.
 */

public class PickBySportResponseModel {
    @SerializedName("Results")
    @Expose
    private PickBySportResultModel results;

    public PickBySportResultModel getResults() {
        return results;
    }

    public void setResults(PickBySportResultModel results) {
        this.results = results;
    }
}
