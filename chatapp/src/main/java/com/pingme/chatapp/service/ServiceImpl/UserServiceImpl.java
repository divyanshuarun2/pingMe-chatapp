package com.pingme.chatapp.service.ServiceImpl;

import com.pingme.chatapp.dto.LoginRequestDto;
import com.pingme.chatapp.dto.LoginResponseDto;
import com.pingme.chatapp.dto.UserDto;
import com.pingme.chatapp.entity.User;
import com.pingme.chatapp.repository.UserRepository;
import com.pingme.chatapp.security.JwtUtil;
import com.pingme.chatapp.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtUtil jwtUtil;

    @Override
    public UserDto saveUser(User user) {
        //check if email or phone is present
        if(userRepository.findByEmail(user.getEmail()).isPresent()){
           // throw new RuntimeException("Email already Exist!");
            return null;
        }
        if( userRepository.findByPhoneNumber(user.getPhoneNumber()).isPresent()){
          //  throw new RuntimeException("Phone number is already used. Please try with new number.");
        return null;
        }

        String encodePassword= passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        try {
            User savedUser = userRepository.save(user);
            return converToUserDto(savedUser);
        }
        catch (Exception e){
            e.printStackTrace();

        }
        return null;

    }

    private UserDto converToUserDto(User savedUser) {
        UserDto userDto= new UserDto();
        userDto.setName(savedUser.getName());
        userDto.setEmail(savedUser.getEmail());
        userDto.setPhoneNumber(savedUser.getPhoneNumber());
        return userDto;

    }
    @Override
    public UserDto userSessionLogin(LoginRequestDto credentials) {
        User savedUser = userRepository.findByEmailOrPhoneNumber(credentials.getUsername(),
                credentials.getUsername()).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"User not found"));

            boolean isPasswordMatch = passwordEncoder.matches(credentials.getPassword(), savedUser.getPassword());
            if(!isPasswordMatch){
                throw new
                        ResponseStatusException(HttpStatus.UNAUTHORIZED,"Invalid Password");
            }

        return converToUserDto(savedUser);

    }
    @Override
    public LoginResponseDto userJwtLogin(LoginRequestDto credentials){

        User user = userRepository.findByEmailOrPhoneNumber(credentials.getUsername(), credentials.getUsername()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Username not Found"));
        if(!passwordEncoder.matches(credentials.getPassword(), user.getPassword())){
          return null;
        }
        String token = jwtUtil.generateToken(user.getEmail());
        return new LoginResponseDto(token,user.getEmail(),"Logged in Successfully");


    }

}
