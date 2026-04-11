package com.pingme.chatapp;

import com.pingme.chatapp.security.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class JwtUtilTest {
    @Autowired
    private JwtUtil jwtUtil;

    @Test
    void shouldGenerateToken(){
        String token = jwtUtil.generateToken("divyanshuarun@gmail.com");
        assertNotNull(token);
        assertFalse(token.isEmpty());

        //jwt has exactly 3 parts
        String[] parts = token.split("\\.");
        assertEquals(3,parts.length);

        System.out.println("Generated Token: ");
        System.out.println(token);
        System.out.println("it has excalty: "+parts.length+" parts");

    }
    @Test
    void shouldValidateUsername(){
        String token = jwtUtil.generateToken("divyanshuarun@gmail.com");
        String username= jwtUtil.extractUsername(token);
        assertEquals(username,"divyanshuarun@gmail.com");
        System.out.println("extracted username : "+username);
    }

    @Test
    void shouldValidateToken(){
        String token = jwtUtil.generateToken("osho@gmail.com");
        Boolean isVlaidate = jwtUtil.validateToken(token, "osho@gmail.com");
        assertTrue(isVlaidate);
        System.out.println("Token is validated");

    }

    @Test
    void shouldRejectWrongUsername(){
        String token = jwtUtil.generateToken("osho@gmail.com");
        Boolean isValidate = jwtUtil.validateToken(token, "hacken@gmail.com");
        assertFalse(isValidate);
        System.out.println("wrong username rejected!");
    }
    @Test
    void shouldRejectTamperedToken() {
        String token = jwtUtil.generateToken("divyanshuarun@gmail.com");
        String tamperedToken = token.substring(0,token.length()-1)+"x";
        Boolean isTokenValid = jwtUtil.validateToken(tamperedToken, "divyanshuarun@gmail.com");
        assertFalse(isTokenValid);
        System.out.println("token is tempered , rejected");


    }
}
