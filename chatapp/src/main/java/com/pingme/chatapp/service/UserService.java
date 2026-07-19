package com.pingme.chatapp.service;

import com.pingme.chatapp.dto.LoginDto;
import com.pingme.chatapp.dto.UserDto;
import com.pingme.chatapp.dto.LoginResponseDto;
import com.pingme.chatapp.entity.User;

public interface UserService {

    UserDto saveUser(User user);
     UserDto userLogin(LoginDto credentials);
     LoginResponseDto jwtLogin(LoginDto credentials);

}
