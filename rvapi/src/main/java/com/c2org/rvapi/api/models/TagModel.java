package com.c2org.rvapi.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TagModel {
    @JsonProperty("tagname")
    private String tagname;

    @JsonProperty("reponame")
    private String reponame;

    public String getTagname() {
        return this.tagname;
    }

    public void setTagname(String tagname) {
        this.tagname = tagname;
    }

    public String getReponame() {
        return this.reponame;
    }

    public void setReponame(String reponame) {
        this.reponame = reponame;
    }
}
