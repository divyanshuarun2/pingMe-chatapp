package com.pingme.chatapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
//@EnableWebSecurity
public class Configurations {


//@Bean
//public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
//    http
//            .csrf(csrf->csrf.disable())
//            .authorizeHttpRequests(auth->auth.
//                    requestMatchers("/h2/console/**").permitAll()
//                    .anyRequest().permitAll())
//            .headers(headers->headers.frameOptions(
//                    frame-> frame.disable()));
//
//    return http.build();
//}


    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }



}
