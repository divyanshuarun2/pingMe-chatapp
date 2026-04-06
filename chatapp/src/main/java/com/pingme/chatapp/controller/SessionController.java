package com.pingme.chatapp.controller;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/session")
public class SessionController {
    @GetMapping("/test")
    public ResponseEntity<?> getSession(HttpServletRequest request){
        HttpSession session= request.getSession(true);
        session.setAttribute("Domain",".thehartford.com");
        return new ResponseEntity<>("your session id is: "+session.getId(), HttpStatus.OK);
    }
}
