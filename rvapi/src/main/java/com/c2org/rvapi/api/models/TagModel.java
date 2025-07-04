package com.c2org.rvapi.api.models;

import java.util.UUID;

public class TagModel {
    private String id;
    private String name;
    private String reponame;
    private String latest_tag;

    public TagModel() {
        this.id = UUID.randomUUID().toString(); // default constructor, autogen uuid for id
    }

    // parameterized constructor
    public TagModel(String name, String reponame, String latest_tag) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.reponame = reponame;
        this.latest_tag = latest_tag;
    }

    // getter & setter
    public String getID() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReponame() {
        return reponame;
    }

    public void setReponame(String reponame) {
        this.reponame = reponame;
    }

    public String getLatestTag() {
        return latest_tag;
    }

    public void setLatestTag(String latest_tag) {
        this.latest_tag = latest_tag;
    }
}
