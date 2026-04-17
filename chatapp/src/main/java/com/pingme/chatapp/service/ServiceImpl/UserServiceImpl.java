package com.pingme.chatapp.service.ServiceImpl;

import com.pingme.chatapp.dto.LoginRequestDto;
import com.pingme.chatapp.dto.LoginResponseDto;
import com.pingme.chatapp.dto.UserDto;
import com.pingme.chatapp.entity.RefreshToken;
import com.pingme.chatapp.entity.UserEntity;
import com.pingme.chatapp.repository.RefreshTokenRepo;
import com.pingme.chatapp.repository.UserRepository;
import com.pingme.chatapp.security.JwtUtil;
import com.pingme.chatapp.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    RefreshTokenRepo refreshTokenRepo;

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
        String refreshToken = jwtUtil.generateRefreshToken(userEntity.getEmail());
        RefreshToken refreshTokenEntity=
                new RefreshToken(refreshToken,userEntity.getEmail(),
                        jwtUtil.getExpiry(refreshToken),
                        false,
                        new Date(System.currentTimeMillis()));

        refreshTokenRepo.save(refreshTokenEntity);
        return new LoginResponseDto(token, userEntity.getEmail(),"Logged in Successfully",refreshToken);
    }

    @Override
    public String getAccessTokenfromRefreshToken(String authHeader) {
        String refreshToken= authHeader.substring(7);
        // 2. Fetch from DB
        RefreshToken refreshTokenFromDb = refreshTokenRepo.findByRefreshToken(refreshToken).orElseThrow(() -> new RuntimeException("Refresh token is not present"));
        // Check revoked
        if (refreshTokenFromDb.isRevoked()) {
            return null; // or throw exception
        }
        //validate the token
        Boolean isValid = jwtUtil.validateToken(refreshToken, refreshTokenFromDb.getUsername());

        // 5. Generate new access token
        if(isValid==true){
            return jwtUtil.generateToken(refreshTokenFromDb.getUsername());
        }
        return null;

        // What should you return if refresh token is invalid?

    }





}
