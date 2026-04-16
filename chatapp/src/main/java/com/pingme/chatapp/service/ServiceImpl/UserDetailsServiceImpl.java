package com.pingme.chatapp.service.ServiceImpl;

import com.pingme.chatapp.entity.UserEntity;
import com.pingme.chatapp.repository.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByEmailOrPhoneNumber(username, username).orElseThrow(() ->
                new UsernameNotFoundException("Provided username is not registered, please register here"));
        // if found -> build UserDetails and return UserDetails
        UserDetails userDetails= User.withUsername(user.getEmail())
                .password(user.getPassword())
                .roles("USER")
                .build();
        return userDetails;
    }
}
