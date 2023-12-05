package com.example.keycloakdemo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/test")
public class Controller {

    @GetMapping("/user")
    public String user() {
        return "Hello user";
    }

    @GetMapping("/admin")
    public String admin() {
        return "Hello admin";
    }
}
