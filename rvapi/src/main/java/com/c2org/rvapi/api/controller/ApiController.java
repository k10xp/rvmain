package com.c2org.rvapi.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {
    @GetMapping
    public String baseRoute() {
        return "Welcome to rvapi";
    }
}
