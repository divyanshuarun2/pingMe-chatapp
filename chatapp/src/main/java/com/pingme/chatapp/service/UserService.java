package com.pingme.chatapp.service;

import com.pingme.chatapp.dto.LoginRequestDto;
import com.pingme.chatapp.dto.UserDto;
import com.pingme.chatapp.entity.UserEntity;
public interface UserService {

    UserDto saveUser(UserEntity userEntity);
    UserDto userSessionLogin(LoginRequestDto credentials);
    String getAccessTokenfromRefreshToken(String refreshToken);
}

