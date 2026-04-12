package com.pingme.chatapp.service;

import com.pingme.chatapp.dto.LoginRequestDto;
import com.pingme.chatapp.dto.LoginResponseDto;
import com.pingme.chatapp.dto.UserDto;
import com.pingme.chatapp.entity.User;

public interface UserService {

    UserDto saveUser(User user);
    UserDto userSessionLogin(LoginRequestDto credentials);
    LoginResponseDto userJwtLogin(LoginRequestDto credentials);
}
