
package com.picksplug.CommonBean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.picksplug.model.PicksResultModel;

public class JsonArrayResponse<T> {

    @SerializedName("Results")
    @Expose
    private PicksResultModel results;

    public PicksResultModel getResults() {
        return results;
    }

    public void setResults(PicksResultModel results) {
        this.results = results;
    }

}
