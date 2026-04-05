package com.pingme.chatapp.controller;

import com.pingme.chatapp.dto.LoginDto;
import com.pingme.chatapp.dto.UserDto;
import com.pingme.chatapp.entity.User;
import com.pingme.chatapp.repository.UserRepository;
import com.pingme.chatapp.service.UserService;
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
    @GetMapping("/me")
    public ResponseEntity<?> getProfile(){
    return new ResponseEntity<String>("This is your profile",HttpStatus.OK);

    //read user from session
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
    public ResponseEntity<String> login(@Valid @RequestBody LoginDto credentials){
        Boolean isSuccessful = userService.userLogin(credentials);
        if(isSuccessful){
            //create session
            //set Set-Cookie: JsessionID=xxx
            //store user info in session
            return new ResponseEntity<>("User credentials are matching, logged-in successful...!"
                    , HttpStatus.OK);
        }
        return new ResponseEntity<>("Invalid Credentails...", HttpStatusCode.valueOf(401));
    }
    @PostMapping("/auth/logout")
    public ResponseEntity<?> logout(){
        return null;
    }

    }
