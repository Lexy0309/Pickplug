package com.picksplug.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by archive_infotech on 26/6/18.
 */

public class PickDetailModel implements Serializable{
    @SerializedName("Id")
    @Expose
    private String id;
    @SerializedName("SportId")
    @Expose
    private String sportId;
    @SerializedName("PickDate")
    @Expose
    private String pickDate;
    @SerializedName("PickdateNew")
    @Expose
    private String pickdateNew;
    @SerializedName("pickTime")
    @Expose
    private String pickTime;
    @SerializedName("WeekDate")
    @Expose
    private String weekDate;
    @SerializedName("WeekTime")
    @Expose
    private String weekTime;
    @SerializedName("VisitingTeam")
    @Expose
    private String visitingTeam;
    @SerializedName("HomeTeam")
    @Expose
    private String homeTeam;
    @SerializedName("PickAnalysis")
    @Expose
    private String pickAnalysis;
    @SerializedName("PickTitle")
    @Expose
    private String pickTitle;
    @SerializedName("PickRecord")
    @Expose
    private String pickRecord;
    @SerializedName("PickStatus")
    @Expose
    private String pickStatus;
    @SerializedName("ModifiedDate")
    @Expose
    private String modifiedDate;
    @SerializedName("HomeTeamDetails")
    @Expose
    private HomeTeamDetailsModel homeTeamDetails;
    @SerializedName("VisitingTeamDetails")
    @Expose
    private VisitingTeamDetailsModel visitingTeamDetails;

    public PickDetailModel(String id, String sportId, String pickDate, String pickdateNew, String pickTime, String weekDate, String weekTime, String visitingTeam, String homeTeam, String pickAnalysis, String pickTitle, String pickRecord, String pickStatus, String modifiedDate, HomeTeamDetailsModel homeTeamDetails, VisitingTeamDetailsModel visitingTeamDetails) {
        this.id = id;
        this.sportId = sportId;
        this.pickDate = pickDate;
        this.pickdateNew = pickdateNew;
        this.pickTime = pickTime;
        this.weekDate = weekDate;
        this.weekTime = weekTime;
        this.visitingTeam = visitingTeam;
        this.homeTeam = homeTeam;
        this.pickAnalysis = pickAnalysis;
        this.pickTitle = pickTitle;
        this.pickRecord = pickRecord;
        this.pickStatus = pickStatus;
        this.modifiedDate = modifiedDate;
        this.homeTeamDetails = homeTeamDetails;
        this.visitingTeamDetails = visitingTeamDetails;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSportId() {
        return sportId;
    }

    public void setSportId(String sportId) {
        this.sportId = sportId;
    }

    public String getPickDate() {
        return pickDate;
    }

    public void setPickDate(String pickDate) {
        this.pickDate = pickDate;
    }

    public String getPickdateNew() {
        return pickdateNew;
    }

    public void setPickdateNew(String pickdateNew) {
        this.pickdateNew = pickdateNew;
    }

    public String getPickTime() {
        return pickTime;
    }

    public void setPickTime(String pickTime) {
        this.pickTime = pickTime;
    }

    public String getWeekDate() {
        return weekDate;
    }

    public void setWeekDate(String weekDate) {
        this.weekDate = weekDate;
    }

    public String getWeekTime() {
        return weekTime;
    }

    public void setWeekTime(String weekTime) {
        this.weekTime = weekTime;
    }

    public String getVisitingTeam() {
        return visitingTeam;
    }

    public void setVisitingTeam(String visitingTeam) {
        this.visitingTeam = visitingTeam;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(String homeTeam) {
        this.homeTeam = homeTeam;
    }

    public String getPickAnalysis() {
        return pickAnalysis;
    }

    public void setPickAnalysis(String pickAnalysis) {
        this.pickAnalysis = pickAnalysis;
    }

    public String getPickTitle() {
        return pickTitle;
    }

    public void setPickTitle(String pickTitle) {
        this.pickTitle = pickTitle;
    }

    public String getPickRecord() {
        return pickRecord;
    }

    public void setPickRecord(String pickRecord) {
        this.pickRecord = pickRecord;
    }

    public String getPickStatus() {
        return pickStatus;
    }

    public void setPickStatus(String pickStatus) {
        this.pickStatus = pickStatus;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public HomeTeamDetailsModel getHomeTeamDetails() {
        return homeTeamDetails;
    }

    public void setHomeTeamDetails(HomeTeamDetailsModel homeTeamDetails) {
        this.homeTeamDetails = homeTeamDetails;
    }

    public VisitingTeamDetailsModel getVisitingTeamDetails() {
        return visitingTeamDetails;
    }

    public void setVisitingTeamDetails(VisitingTeamDetailsModel visitingTeamDetails) {
        this.visitingTeamDetails = visitingTeamDetails;
    }

}
