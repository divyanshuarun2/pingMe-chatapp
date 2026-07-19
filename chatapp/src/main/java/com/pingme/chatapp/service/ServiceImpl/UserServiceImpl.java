package com.pingme.chatapp.service.ServiceImpl;

import com.pingme.chatapp.dto.LoginDto;
import com.pingme.chatapp.dto.LoginResponseDto;
import com.pingme.chatapp.dto.UserDto;
import com.pingme.chatapp.entity.User;
import com.pingme.chatapp.repository.UserRepository;
import com.pingme.chatapp.security.JwtUtil;
import com.pingme.chatapp.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final String successMessage= "You have logged in successfully!!";

    UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil=jwtUtil;
    }

    @Override
    public UserDto saveUser(User user) {
        //check if email or phone is present
        if(userRepository.findByEmail(user.getEmail()).isPresent()){
           // throw new RuntimeException("Email already Exist!");
            return null;
        }
        if(userRepository.findByPhoneNumber(user.getPhoneNumber()).isPresent()){
          //  throw new RuntimeException("Phone number is already used. Please try with new number.");
        return null;
        }

        String encodePassword= passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        try {
            User savedUser = userRepository.save(user);
            return convertToUserDto(savedUser);
        }
        catch (Exception e){
            e.printStackTrace();

        }
        return null;

    }

    private UserDto convertToUserDto(User savedUser) {
        UserDto userDto= new UserDto();
        userDto.setName(savedUser.getName());
        userDto.setEmail(savedUser.getEmail());
        userDto.setPhoneNumber(savedUser.getPhoneNumber());
        return userDto;

    }
    // this method is used  for session based login
    public UserDto userLogin(LoginDto credentails) {
        User savedUser = userRepository.findByEmailOrPhoneNumber(credentails.getUsername(),
                credentails.getUsername()).orElse(null);
        if(savedUser!=null){
            boolean isPasswordMatch = passwordEncoder.matches(credentails.getPassword(), savedUser.getPassword());
            if(isPasswordMatch){
                return convertToUserDto(savedUser);
            }
        }
        return null;
    }

    @Override
    public LoginResponseDto jwtLogin(LoginDto credentials) {
        User savedUser = userRepository.findByEmailOrPhoneNumber(credentials.getUsername(),
                credentials.getUsername()).orElse(null);
        if (savedUser != null) {
            boolean isPasswordMatch = passwordEncoder.matches(credentials.getPassword(), savedUser.getPassword());
            if (isPasswordMatch) {
              //create token
                String accessToken = jwtUtil.createAccessToken(savedUser.getEmail());
                //create login response and return

                return new LoginResponseDto(accessToken, savedUser.getEmail(),successMessage);
            }
            return new LoginResponseDto("",savedUser.getEmail(),"Invalid Password");
        }
        return  new LoginResponseDto("",credentials.getUsername(),"! No account found with given username or phone no.");
    }
    }


