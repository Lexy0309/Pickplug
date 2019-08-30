package com.picksplug.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by archive_infotech on 26/6/18.
 */

public class HomeTeamDetailsModel implements Serializable{
    @SerializedName("TeamName")
    @Expose
    private String teamName;
    @SerializedName("TeamIcon")
    @Expose
    private String teamIcon;

    // Decodes home team details json into Home Team Details Model
    public static HomeTeamDetailsModel fromJson(JSONObject jsonObject) {
        HomeTeamDetailsModel teamDetailsModel = new HomeTeamDetailsModel();
        // Deserialize json into object fields
        try {
            teamDetailsModel.teamName        =   jsonObject.getString("TeamName");
            teamDetailsModel.teamIcon        =   jsonObject.getString("TeamIcon");

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        // Return new object
        return teamDetailsModel;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getTeamIcon() {
        return teamIcon;
    }

    public void setTeamIcon(String teamIcon) {
        this.teamIcon = teamIcon;
    }

}
