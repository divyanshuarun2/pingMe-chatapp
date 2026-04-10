package com.pingme.chatapp.controller;

import com.pingme.chatapp.dto.LoginDto;
import com.pingme.chatapp.dto.UserDto;
import com.pingme.chatapp.entity.SessionEntity;
import com.pingme.chatapp.entity.User;

import com.pingme.chatapp.service.SessionService;
import com.pingme.chatapp.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private SessionService sessionService;

    @GetMapping("/health")
    public String getHealth() {
        return "ping me application up and running!";
    }

    @GetMapping("/auth/me")
    public ResponseEntity<?> getProfile(HttpServletRequest request){

        HttpSession session = request.getSession(false);

        if (session == null) {
            return ResponseEntity.status(401).body("Session expired or not logged in");
        }

        String userId = (String) session.getAttribute("userId");

        SessionEntity existingSession = sessionService.getSession(userId);

        if (existingSession == null) {
            return ResponseEntity.status(401).body("Session expired, please login again");
        }

        // Check if DB session matches server session
        if (existingSession.getJsessionId().equals(session.getId())) {
            return ResponseEntity.ok("Profile for: " + userId);
        }

        // DB session exists but different JSESSIONID → multiple login detection
        return ResponseEntity.status(401).body("You were logged in from another device. Please login again.");
    }

    @PostMapping("/auth/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response){
        HttpSession session = request.getSession(false);
        //user is logged in
        if(session!=null){
        //removing session entry from DB
            Boolean isSessionDeleted =  sessionService.deleteSessionEntry((String)session.getAttribute("userId"));
            //removing session memory
            session.invalidate();
           if(isSessionDeleted){
               return new ResponseEntity<>("Logged-out successfully",HttpStatus.OK);
           }
             }

            return new ResponseEntity<>("Not active user, Please login first",HttpStatusCode.valueOf(400));

    }


    @PostMapping("/auth/signup")
    public ResponseEntity<UserDto> signUp(@Valid @RequestBody User user) {
        UserDto userDto = userService.saveUser(user);
        if(userDto!=null){
            return new ResponseEntity<>(userDto, HttpStatus.CREATED);
        }
return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDto credentials, HttpServletRequest request){
        UserDto isSuccessful = userService.userLogin(credentials);
        if(isSuccessful!=null){
            //create session-- set Set-Cookie: JsessionID=xxx
            HttpSession session = request.getSession(true);
            session.setAttribute("userId",isSuccessful.getEmail());

            //Store session in DB associated with userId
            SessionEntity userSession = new SessionEntity(isSuccessful.getEmail(),session.getId());
            //every time new login-save the session

            sessionService.saveSession(userSession);

            return new ResponseEntity<>("User credentials are matching, logged-in successful...!"
                    , HttpStatus.OK);
        }
        return new ResponseEntity<>("Invalid Credentails...", HttpStatusCode.valueOf(401));
    }

    }
