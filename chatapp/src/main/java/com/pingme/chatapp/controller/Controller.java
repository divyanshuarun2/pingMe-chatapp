package com.pingme.chatapp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pingme/api")
public class Controller {
    @GetMapping("/health")
    public String getHealth(){
        return "ping me application up and running!";
    }
}
