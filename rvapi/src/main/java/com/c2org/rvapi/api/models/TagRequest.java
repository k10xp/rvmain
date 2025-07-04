package com.c2org.rvapi.api.models;

public class TagRequest{
    private String name;
    private String reponame;
    private String latest_tag = "";

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

    public String getLatest_tag() {
        return latest_tag;
    }

    public void setLatest_tag(String latest_tag) {
        this.latest_tag = latest_tag;
    }
}