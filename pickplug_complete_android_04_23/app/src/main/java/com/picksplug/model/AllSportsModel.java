package com.picksplug.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by archive_infotech on 7/10/18.
 */

public class AllSportsModel implements Serializable{
    @SerializedName("Id")
    @Expose
    private String id;
    @SerializedName("SportName")
    @Expose
    private String sportName;
    @SerializedName("Ranking")
    @Expose
    private String ranking;
    @SerializedName("SportIcon")
    @Expose
    private String sportIcon;
    @SerializedName("thumb")
    @Expose
    private String thumb;
    @SerializedName("AddedDate")
    @Expose
    private String addedDate;
    @SerializedName("ModifiedDate")
    @Expose
    private String modifiedDate;
    @SerializedName("Status")
    @Expose
    private String status;
    @SerializedName("RecordNo")
    @Expose
    private String recordNo;
    @SerializedName("IsDeleted")
    @Expose
    private String isDeleted;
    @SerializedName("count")
    @Expose
    private String count;

    public AllSportsModel(String id, String sportName, String ranking, String sportIcon, String thumb, String addedDate, String modifiedDate, String status, String recordNo, String isDeleted, String count) {
        this.id = id;
        this.sportName = sportName;
        this.ranking = ranking;
        this.sportIcon = sportIcon;
        this.thumb = thumb;
        this.addedDate = addedDate;
        this.modifiedDate = modifiedDate;
        this.status = status;
        this.recordNo = recordNo;
        this.isDeleted = isDeleted;
        this.count = count;
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

    public String getRanking() {
        return ranking;
    }

    public void setRanking(String ranking) {
        this.ranking = ranking;
    }

    public String getSportIcon() {
        return sportIcon;
    }

    public void setSportIcon(String sportIcon) {
        this.sportIcon = sportIcon;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRecordNo() {
        return recordNo;
    }

    public void setRecordNo(String recordNo) {
        this.recordNo = recordNo;
    }

    public String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
