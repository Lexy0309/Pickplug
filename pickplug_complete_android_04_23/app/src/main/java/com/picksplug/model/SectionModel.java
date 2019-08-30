package com.picksplug.model;

import java.util.ArrayList;

/**
 * Created by archive_infotech on 7/14/18.
 */

public class SectionModel {
    private String                      sectionHeader;
    private ArrayList<PicksDetailModel>  pickDetailModelArrayList;

    public SectionModel(String sectionHeader, ArrayList<PicksDetailModel> pickDetailModelArrayList) {
        this.sectionHeader = sectionHeader;
        this.pickDetailModelArrayList = pickDetailModelArrayList;
    }

    public String getSectionHeader() {
        return sectionHeader;
    }

    public void setSectionHeader(String sectionHeader) {
        this.sectionHeader = sectionHeader;
    }

    public ArrayList<PicksDetailModel> getPickDetailModelArrayList() {
        return pickDetailModelArrayList;
    }

    public void setPickDetailModelArrayList(ArrayList<PicksDetailModel> pickDetailModelArrayList) {
        this.pickDetailModelArrayList = pickDetailModelArrayList;
    }
}
