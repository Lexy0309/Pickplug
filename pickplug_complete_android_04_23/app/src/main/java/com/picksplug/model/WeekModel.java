package com.picksplug.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by archive_infotech on 7/20/18.
 */

public class WeekModel {
    @SerializedName("Id")
    @Expose
    private String id;
    @SerializedName("sportImage")
    @Expose
    private String sportImage;
    @SerializedName("SportId")
    @Expose
    private String sportId;
    @SerializedName("Week")
    @Expose
    private String week;
    @SerializedName("StartDate")
    @Expose
    private String startDate;
    @SerializedName("EndDate")
    @Expose
    private String endDate;
    @SerializedName("PickDate")
    @Expose
    private String pickDate;
    @SerializedName("pickTime")
    @Expose
    private String pickTime;
    @SerializedName("pickdateNew")
    @Expose
    private String pickdateNew;
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
    @SerializedName("AddedDate")
    @Expose
    private String addedDate;
    @SerializedName("HomeTeamDetailsModel")
    @Expose
    private HomeTeamDetailsModel HomeTeamDetailsModel;
    @SerializedName("VisitingTeamDetailsModel")
    @Expose
    private VisitingTeamDetailsModel VisitingTeamDetailsModel;
    @SerializedName("FreePick")
    @Expose
    private String freePick;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSportImage() {
        return sportImage;
    }

    public void setSportImage(String sportImage) {
        this.sportImage = sportImage;
    }

    public String getSportId() {
        return sportId;
    }

    public void setSportId(String sportId) {
        this.sportId = sportId;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getPickDate() {
        return pickDate;
    }

    public void setPickDate(String pickDate) {
        this.pickDate = pickDate;
    }

    public String getPickTime() {
        return pickTime;
    }

    public void setPickTime(String pickTime) {
        this.pickTime = pickTime;
    }

    public String getPickdateNew() {
        return pickdateNew;
    }

    public void setPickdateNew(String pickdateNew) {
        this.pickdateNew = pickdateNew;
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

    public String getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(String addedDate) {
        this.addedDate = addedDate;
    }

    public HomeTeamDetailsModel getHomeTeamDetailsModel() {
        return HomeTeamDetailsModel;
    }

    public void setHomeTeamDetailsModel(HomeTeamDetailsModel HomeTeamDetailsModel) {
        this.HomeTeamDetailsModel = HomeTeamDetailsModel;
    }

    public VisitingTeamDetailsModel getVisitingTeamDetailsModel() {
        return VisitingTeamDetailsModel;
    }

    public void setVisitingTeamDetailsModel(VisitingTeamDetailsModel VisitingTeamDetailsModel) {
        this.VisitingTeamDetailsModel = VisitingTeamDetailsModel;
    }

    public String getFreePick() {
        return freePick;
    }

    public void setFreePick(String freePick) {
        this.freePick = freePick;
    }

}
