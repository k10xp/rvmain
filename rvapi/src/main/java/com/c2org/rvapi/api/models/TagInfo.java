package com.c2org.rvapi.api.models;

public class TagInfo extends TagModel {
    private String tag_id;
    private String user_id;
    private String latest_tag;
    private String tag_created;
    private String tag_updated;
    private boolean is_show;

    public String getTagId() {
        return tag_id;
    }

    public void setTagId(String tag_id) {
        this.tag_id = tag_id;
    }

    public String getUserId() {
        return this.user_id;
    }

    public void setUserId(String user_id) {
        this.user_id = user_id;
    }

    public String getLatestTag() {
        return this.latest_tag;
    }

    public void setLatestTag(String latest_tag) {
        this.latest_tag = latest_tag;
    }

    public String getCreated() {
        return this.tag_created;
    }

    public void setCreated(String tag_created) {
        this.tag_created = tag_created;
    }

    public String getUpdated() {
        return this.tag_updated;
    }

    public void setUpdated(String tag_updated) {
        this.tag_updated = tag_updated;
    }

    public boolean getIsShow() {
        return this.is_show;
    }

    public void setIsShow(boolean is_show) {
        this.is_show = is_show;
    }
}