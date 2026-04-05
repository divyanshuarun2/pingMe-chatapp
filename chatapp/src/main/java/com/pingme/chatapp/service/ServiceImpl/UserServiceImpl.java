package com.pingme.chatapp.service.ServiceImpl;

import com.pingme.chatapp.dto.LoginDto;
import com.pingme.chatapp.dto.UserDto;
import com.pingme.chatapp.entity.User;
import com.pingme.chatapp.repository.UserRepository;
import com.pingme.chatapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

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
    public Boolean userLogin(LoginDto credentails) {
        User savedUser = userRepository.findByEmailOrPhoneNumber(credentails.getUsername(),
                credentails.getUsername()).orElse(null);
        if(savedUser!=null){
            boolean isPasswordMatch = passwordEncoder.matches(credentails.getPassword(), savedUser.getPassword());
            if(isPasswordMatch){
                return true;
            }
        }
        return false;

    }
}
