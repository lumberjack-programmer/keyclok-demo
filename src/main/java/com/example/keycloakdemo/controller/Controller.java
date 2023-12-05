package com.example.keycloakdemo.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/test")
public class Controller {

    @GetMapping("/user")
    @PreAuthorize("hasRole('client_user')")
    public String user() {
        return "Hello user";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('client_admin')")
    public String admin() {
        return "Hello admin";
    }
}
