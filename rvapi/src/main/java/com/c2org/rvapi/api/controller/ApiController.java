package com.c2org.rvapi.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.c2org.rvapi.api.models.TagModel;
import com.c2org.rvapi.api.models.TagRequest;

@RestController
public class ApiController {
    @GetMapping
    public String baseRoute() {
        return "Welcome to rvapi";
    }

    @GetMapping("/example/info")
    public TagModel getUserInfo() {
        return new TagModel("name", "reponame", "latest_tag");
    }

    @PostMapping("/example/create")
    public TagModel createExample(@RequestBody TagRequest tagReq) {
        // create new TagModel
        TagModel tagModel = new TagModel();
        tagModel.setName(tagReq.getName());
        tagModel.setReponame(tagReq.getReponame());
        tagModel.setLatestTag(tagReq.getLatest_tag());

        return tagModel;
    }
}
