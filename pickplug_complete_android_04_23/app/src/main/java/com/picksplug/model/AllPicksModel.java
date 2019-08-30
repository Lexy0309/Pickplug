package com.picksplug.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by archive_infotech on 7/10/18.
 */

public class AllPicksModel implements Serializable{
    @SerializedName("Id")
    @Expose
    private String id;
    @SerializedName("SportName")
    @Expose
    private String sportName;
    @SerializedName("VisitingTeam")
    @Expose
    private String visitingTeam;
    @SerializedName("HomeTeam")
    @Expose
    private String homeTeam;
    @SerializedName("PickDate")
    @Expose
    private String pickDate;
    @SerializedName("NflPick")
    @Expose
    private String nflPick;
    @SerializedName("PickTitle")
    @Expose
    private String pickTitle;
    @SerializedName("PickAnalysis")
    @Expose
    private String pickAnalysis;
    @SerializedName("PickStatus")
    @Expose
    private String pickStatus;
    @SerializedName("PickRecord")
    @Expose
    private String pickRecord;
    @SerializedName("WeekDate")
    @Expose
    private String weekDate;
    @SerializedName("AddedDate")
    @Expose
    private String addedDate;
    @SerializedName("ModifiedDate")
    @Expose
    private String modifiedDate;
    @SerializedName("IsDeleted")
    @Expose
    private String isDeleted;
    @SerializedName("Status")
    @Expose
    private String status;
    @SerializedName("FreePick")
    @Expose
    private String freePick;
    @SerializedName("homeTeamName")
    @Expose
    private String homeTeamName;
    @SerializedName("homeTeamIcon")
    @Expose
    private String homeTeamIcon;
    @SerializedName("visitiingTeamName")
    @Expose
    private String visitiingTeamName;
    @SerializedName("visitingTeamIcon")
    @Expose
    private String visitingTeamIcon;

    public AllPicksModel() {
    }

    public AllPicksModel(String id, String sportName, String visitingTeam, String homeTeam, String pickDate, String nflPick, String pickTitle, String pickAnalysis, String pickStatus, String pickRecord, String weekDate, String addedDate, String modifiedDate, String isDeleted, String status, String freePick, String homeTeamName, String homeTeamIcon, String visitiingTeamName, String visitingTeamIcon) {
        this.id = id;
        this.sportName = sportName;
        this.visitingTeam = visitingTeam;
        this.homeTeam = homeTeam;
        this.pickDate = pickDate;
        this.nflPick = nflPick;
        this.pickTitle = pickTitle;
        this.pickAnalysis = pickAnalysis;
        this.pickStatus = pickStatus;
        this.pickRecord = pickRecord;
        this.weekDate = weekDate;
        this.addedDate = addedDate;
        this.modifiedDate = modifiedDate;
        this.isDeleted = isDeleted;
        this.status = status;
        this.freePick = freePick;
        this.homeTeamName = homeTeamName;
        this.homeTeamIcon = homeTeamIcon;
        this.visitiingTeamName = visitiingTeamName;
        this.visitingTeamIcon = visitingTeamIcon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSportName() {
        return sportName;
    }

    public void setSportName(String sportName) {
        this.sportName = sportName;
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

    public String getPickDate() {
        return pickDate;
    }

    public void setPickDate(String pickDate) {
        this.pickDate = pickDate;
    }

    public String getNflPick() {
        return nflPick;
    }

    public void setNflPick(String nflPick) {
        this.nflPick = nflPick;
    }

    public String getPickTitle() {
        return pickTitle;
    }

    public void setPickTitle(String pickTitle) {
        this.pickTitle = pickTitle;
    }

    public String getPickAnalysis() {
        return pickAnalysis;
    }

    public void setPickAnalysis(String pickAnalysis) {
        this.pickAnalysis = pickAnalysis;
    }

    public String getPickStatus() {
        return pickStatus;
    }

    public void setPickStatus(String pickStatus) {
        this.pickStatus = pickStatus;
    }

    public String getPickRecord() {
        return pickRecord;
    }

    public void setPickRecord(String pickRecord) {
        this.pickRecord = pickRecord;
    }

    public String getWeekDate() {
        return weekDate;
    }

    public void setWeekDate(String weekDate) {
        this.weekDate = weekDate;
    }

    public String getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(String addedDate) {
        this.addedDate = addedDate;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFreePick() {
        return freePick;
    }

    public void setFreePick(String freePick) {
        this.freePick = freePick;
    }

    public String getHomeTeamName() {
        return homeTeamName;
    }

    public void setHomeTeamName(String homeTeamName) {
        this.homeTeamName = homeTeamName;
    }

    public String getHomeTeamIcon() {
        return homeTeamIcon;
    }

    public void setHomeTeamIcon(String homeTeamIcon) {
        this.homeTeamIcon = homeTeamIcon;
    }

    public String getVisitiingTeamName() {
        return visitiingTeamName;
    }

    public void setVisitiingTeamName(String visitiingTeamName) {
        this.visitiingTeamName = visitiingTeamName;
    }

    public String getVisitingTeamIcon() {
        return visitingTeamIcon;
    }

    public void setVisitingTeamIcon(String visitingTeamIcon) {
        this.visitingTeamIcon = visitingTeamIcon;
    }
}
