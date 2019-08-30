package com.picksplug.drawer_click;

/**
 * Created by Ravi on 29/07/15.
 */
public class NavDrawerItem
{
    private boolean showNotify;
    private String title;
    private int imageId;
    private int selImageId;
    private boolean isSelected;
    private Boolean IsHeader ;


    public NavDrawerItem() {

    }

    public NavDrawerItem(boolean showNotify, Boolean isHeader, int imageId, int selImageId, boolean isSelected, String title) {
        this.showNotify = showNotify;
        IsHeader = isHeader;
        this.imageId = imageId;
        this.selImageId = selImageId;
        this.isSelected = isSelected;
        this.title = title;
    }

    public Boolean getIsHeader() {
        return IsHeader;
    }

    public void setIsHeader(Boolean isHeader) {
        IsHeader = isHeader;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public boolean isShowNotify() {
        return showNotify;
    }

    public void setShowNotify(boolean showNotify) {
        this.showNotify = showNotify;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public int getSelImageId() {
        return selImageId;
    }

    public void setSelImageId(int selImageId) {
        this.selImageId = selImageId;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
