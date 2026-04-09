package com.pingme.chatapp.controller;

import com.pingme.chatapp.dto.LoginDto;
import com.pingme.chatapp.dto.UserDto;
import com.pingme.chatapp.entity.SessionEntity;
import com.pingme.chatapp.entity.User;
import com.pingme.chatapp.repository.UserRepository;
import com.pingme.chatapp.service.SessionService;
import com.pingme.chatapp.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;


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
        HttpSession session = request.getSession(false);// getting from server memory
        if(session!=null){
            // session exists for incoming JsessionId

            //checking if session exists for the incoming cookie/jsessionid
            SessionEntity existingSession = sessionService.getSession((String)session.getAttribute("userId"));
            System.out.println("fetching session from db to check if any session of this user exists?");

            if(existingSession!=null && existingSession.getJsessionId().equals(session.getId())){
                System.out.println("db fetched sessionId: "+existingSession.getJsessionId()+" and server session "+session.getId());
                return new ResponseEntity<>("This is your profile: "+session.getAttribute("userId"),HttpStatus.OK);

            } else if (existingSession!=null && existingSession.getJsessionId()!=null && !existingSession.getJsessionId().equals(session.getId())) {
                return new ResponseEntity<>("You hav been logged in from other device, please login again "+session.getAttribute("userId"),HttpStatus.UNAUTHORIZED);
            }
            //if not then returned sesion expired... please login again
        }
        return new ResponseEntity<>("You are not logged in or session is expired",HttpStatusCode.valueOf(401));
//
//    //read user from session
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

//        Cookie cookie= new Cookie("JSESSIONID","");
//        cookie.setPath("/");
//        cookie.setMaxAge(0);
//        response.addCookie(cookie);

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
