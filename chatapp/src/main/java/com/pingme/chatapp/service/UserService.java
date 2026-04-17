package com.pingme.chatapp.service;

import com.pingme.chatapp.dto.LoginRequestDto;
import com.pingme.chatapp.dto.LoginResponseDto;
import com.pingme.chatapp.dto.UserDto;
import com.pingme.chatapp.entity.UserEntity;
import org.springframework.http.ResponseEntity;

public interface UserService {

    UserDto saveUser(UserEntity userEntity);
    UserDto userSessionLogin(LoginRequestDto credentials);
    LoginResponseDto userJwtLogin(LoginRequestDto credentials);

    String getAccessTokenfromRefreshToken(String refreshToken);
}
