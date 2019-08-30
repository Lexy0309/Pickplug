package com.picksplug.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by archive_infotech on 26/6/18.
 */

public class SportsDetailModel implements Serializable{
    @SerializedName("Id")
    @Expose
    private String id;
    @SerializedName("SportName")
    @Expose
    private String sportName;
    @SerializedName("RecordNo")
    @Expose
    private String recordNo;
    @SerializedName("SportImage")
    @Expose
    private String sportImage;
    @SerializedName("SportThumb")
    @Expose
    private String sportThumb;
    @SerializedName("AddedDate")
    @Expose
    private String addedDate;
    @SerializedName("ModifiedDate")
    @Expose
    private String modifiedDate;

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

    public String getRecordNo() {
        return recordNo;
    }

    public void setRecordNo(String recordNo) {
        this.recordNo = recordNo;
    }

    public String getSportImage() {
        return sportImage;
    }

    public void setSportImage(String sportImage) {
        this.sportImage = sportImage;
    }

    public String getSportThumb() {
        return sportThumb;
    }

    public void setSportThumb(String sportThumb) {
        this.sportThumb = sportThumb;
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
}
