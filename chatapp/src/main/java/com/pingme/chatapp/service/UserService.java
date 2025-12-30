package com.pingme.chatapp.service;

import com.pingme.chatapp.dto.LoginDto;
import com.pingme.chatapp.dto.UserDto;
import com.pingme.chatapp.entity.User;

public interface UserService {

    public UserDto saveUser(User user);
    public Boolean userLogin(LoginDto credentials);
}
