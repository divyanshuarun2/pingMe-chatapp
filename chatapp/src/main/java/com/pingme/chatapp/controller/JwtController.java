package com.pingme.chatapp.controller;

import com.pingme.chatapp.dto.LoginRequestDto;
import com.pingme.chatapp.dto.LoginResponseDto;
import com.pingme.chatapp.dto.UserDto;
import com.pingme.chatapp.entity.UserEntity;
import com.pingme.chatapp.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/jwt")
public class JwtController {
    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<UserDto> signUp(@Valid @RequestBody UserEntity userEntity) {
        UserDto userDto = userService.saveUser(userEntity);
        if(userDto!=null){
            return new ResponseEntity<>(userDto, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto credentials){
        try{
            LoginResponseDto response = userService.userJwtLogin(credentials);
            return ResponseEntity.ok(response);
        }
        catch (RuntimeException e){
            return ResponseEntity.badRequest().build();

        }

    }
    @GetMapping("/me")
    public ResponseEntity<?> me(@AuthenticationPrincipal UserDetails userDetails){
        if(userDetails!=null)
        return ResponseEntity.ok("Logged in as: "+userDetails.getUsername());
        return ResponseEntity.status(401).body("Not Authenticated");

    }
}
