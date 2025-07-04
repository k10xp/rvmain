package com.c2org.rvapi.api.models;

public class TagRequest {
    private String name;
    private String reponame;
    private String latest_tag = null;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReponame() {
        return this.reponame;
    }

    public void setReponame(String reponame) {
        this.reponame = reponame;
    }

    public String getLatest_tag() {
        return this.latest_tag;
    }

    public void setLatest_tag(String latest_tag) {
        this.latest_tag = latest_tag;
    }
}