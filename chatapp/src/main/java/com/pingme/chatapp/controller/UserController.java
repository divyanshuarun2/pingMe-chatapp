package com.pingme.chatapp.controller;

import com.pingme.chatapp.dto.LoginDto;
import com.pingme.chatapp.dto.UserDto;
import com.pingme.chatapp.entity.User;
import com.pingme.chatapp.repository.UserRepository;
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


@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/health")
    public String getHealth() {
        return "ping me application up and running!";
    }

    @GetMapping("/auth/me")
    public ResponseEntity<?> getProfile(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if(session!=null){
            return new ResponseEntity<>("This is your profile: "+session.getAttribute("userId"),HttpStatus.OK);
        }
        return new ResponseEntity<>("You are not logged in",HttpStatusCode.valueOf(401));

    //read user from session
    }

    @PostMapping("/auth/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response){
        HttpSession session = request.getSession(false);
        //user is logged in
        if(session!=null){ session.invalidate();}
        else {
            return new ResponseEntity<>("Not active user, Please login first",HttpStatusCode.valueOf(400));
        }
        Cookie cookie= new Cookie("JSESSIONID","");
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return new ResponseEntity<>("Logged-out successfully",HttpStatus.OK);
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
            //create session
            HttpSession session = request.getSession(true);
            session.setAttribute("userId",isSuccessful.getEmail());
            session.setAttribute("Domain",".thehartford.com");
            //set Set-Cookie: JsessionID=xxx
            //store user info in session
            return new ResponseEntity<>("User credentials are matching, logged-in successful...!"
                    , HttpStatus.OK);
        }
        return new ResponseEntity<>("Invalid Credentails...", HttpStatusCode.valueOf(401));
    }

    }
