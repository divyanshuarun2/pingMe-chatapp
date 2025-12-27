package com.pingme.chatapp.controller;

import com.pingme.chatapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class Controller {
    @Autowired
    private UserRepository userRepo;

    @GetMapping("/health")
    public String getHealth(){
        return "ping me application up and running!";
    }


}
