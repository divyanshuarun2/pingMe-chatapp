package com.pingme.chatapp.service.ServiceImpl;

import com.pingme.chatapp.dto.LoginRequestDto;
import com.pingme.chatapp.dto.LoginResponseDto;
import com.pingme.chatapp.dto.UserDto;
import com.pingme.chatapp.entity.UserEntity;
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
    public UserDto saveUser(UserEntity userEntity) {
        //check if email or phone is present
        if(userRepository.findByEmail(userEntity.getEmail()).isPresent()){
           // throw new RuntimeException("Email already Exist!");
            return null;
        }
        if( userRepository.findByPhoneNumber(userEntity.getPhoneNumber()).isPresent()){
          //  throw new RuntimeException("Phone number is already used. Please try with new number.");
        return null;
        }

        String encodePassword= passwordEncoder.encode(userEntity.getPassword());
        userEntity.setPassword(encodePassword);
        try {
            UserEntity savedUserEntity = userRepository.save(userEntity);
            return converToUserDto(savedUserEntity);
        }
        catch (Exception e){
            e.printStackTrace();

        }
        return null;

    }

    private UserDto converToUserDto(UserEntity savedUserEntity) {
        UserDto userDto= new UserDto();
        userDto.setName(savedUserEntity.getName());
        userDto.setEmail(savedUserEntity.getEmail());
        userDto.setPhoneNumber(savedUserEntity.getPhoneNumber());
        return userDto;

    }
    @Override
    public UserDto userSessionLogin(LoginRequestDto credentials) {
        UserEntity savedUserEntity = userRepository.findByEmailOrPhoneNumber(credentials.getUsername(),
                credentials.getUsername()).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"User not found"));

            boolean isPasswordMatch = passwordEncoder.matches(credentials.getPassword(), savedUserEntity.getPassword());
            if(!isPasswordMatch){
                throw new
                        ResponseStatusException(HttpStatus.UNAUTHORIZED,"Invalid Password");
            }

        return converToUserDto(savedUserEntity);

    }
    @Override
    public LoginResponseDto userJwtLogin(LoginRequestDto credentials){

        UserEntity userEntity = userRepository.findByEmailOrPhoneNumber(credentials.getUsername(), credentials.getUsername()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Username not Found"));
        if(!passwordEncoder.matches(credentials.getPassword(), userEntity.getPassword())){
          return null;
        }
        String token = jwtUtil.generateToken(userEntity.getEmail());
        return new LoginResponseDto(token, userEntity.getEmail(),"Logged in Successfully");


    }

}
