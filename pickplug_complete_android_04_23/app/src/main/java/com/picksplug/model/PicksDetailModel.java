package com.picksplug.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by archive_infotech on 7/14/18.
 */

public class PicksDetailModel implements Serializable{
    @SerializedName("FreePick")
    @Expose
    private String freePick;
    @SerializedName("Id")
    @Expose
    private String id;
    @SerializedName("sportImage")
    @Expose
    private String sportImage;
    @SerializedName("SportId")
    @Expose
    private String sportId;
    @SerializedName("PickDate")
    @Expose
    private String pickDate;
    @SerializedName("pickTime")
    @Expose
    private String pickTime;
    @SerializedName("pickdateNew")
    @Expose
    private String pickdateNew;
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

    //for NFL weeks
    private String week;
    private String weekDate;

    public PicksDetailModel(String freePick, String id, String sportImage, String sportId, String pickDate, String pickTime, String pickdateNew, String visitingTeam, String homeTeam, String pickAnalysis, String pickTitle, String pickRecord, String pickStatus, String modifiedDate, HomeTeamDetailsModel homeTeamDetails, VisitingTeamDetailsModel visitingTeamDetails) {
        this.freePick = freePick;
        this.id = id;
        this.sportImage = sportImage;
        this.sportId = sportId;
        this.pickDate = pickDate;
        this.pickTime = pickTime;
        this.pickdateNew = pickdateNew;
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

    public PicksDetailModel(String freePick, String id, String sportImage, String sportId, String pickDate, String pickTime, String pickdateNew, String visitingTeam, String homeTeam, String pickAnalysis, String pickTitle, String pickRecord, String pickStatus, String modifiedDate, HomeTeamDetailsModel homeTeamDetails, VisitingTeamDetailsModel visitingTeamDetails, String week, String weekDate) {
        this.freePick = freePick;
        this.id = id;
        this.sportImage = sportImage;
        this.sportId = sportId;
        this.pickDate = pickDate;
        this.pickTime = pickTime;
        this.pickdateNew = pickdateNew;
        this.visitingTeam = visitingTeam;
        this.homeTeam = homeTeam;
        this.pickAnalysis = pickAnalysis;
        this.pickTitle = pickTitle;
        this.pickRecord = pickRecord;
        this.pickStatus = pickStatus;
        this.modifiedDate = modifiedDate;
        this.homeTeamDetails = homeTeamDetails;
        this.visitingTeamDetails = visitingTeamDetails;
        this.week = week;
        this.weekDate = weekDate;
    }

    public String getFreePick() {
        return freePick;
    }

    public void setFreePick(String freePick) {
        this.freePick = freePick;
    }

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

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getWeekDate() {
        return weekDate;
    }

    public void setWeekDate(String weekDate) {
        this.weekDate = weekDate;
    }
}
