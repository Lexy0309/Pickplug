package com.picksplug.model;

/**
 * Created by archive_infotech on 4/7/18.
 */

public class SportsBookModel {
    private String id;
    private String image_url;
    private String join_url;
    private String title;
    private String description;
    private String operating;

    public SportsBookModel(String id, String image_url, String join_url, String title, String description, String operating) {
        this.id = id;
        this.image_url = image_url;
        this.join_url = join_url;
        this.title = title;
        this.description = description;
        this.operating = operating;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getJoin_url() {
        return join_url;
    }

    public void setJoin_url(String join_url) {
        this.join_url = join_url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOperating() {
        return operating;
    }

    public void setOperating(String operating) {
        this.operating = operating;
    }
}
