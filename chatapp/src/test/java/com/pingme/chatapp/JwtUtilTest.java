package com.pingme.chatapp;

import com.pingme.chatapp.security.JwtUtil;
import org.junit.jupiter.api.AutoClose;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class JwtUtilTest {
    @Autowired
    private JwtUtil jwtUtil;


    @Test
    void shouldGeneratejwtToken(){
        String accessToken = jwtUtil.createAccessToken("divyanshuarun@gmail.com");
        assertNotNull(accessToken);
        assertFalse(accessToken.isEmpty());
        System.out.println("{accessToken:\n"+accessToken+"\n}");
        String[] split = accessToken.split("\\.");
        assertEquals(3,split.length);
        System.out.println(split.length);

    }
    @Test
    public void shouldReturnClaims(){
        String token = jwtUtil.createAccessToken("osho@gmail.com");
        Object allClaims = jwtUtil.getAllClaims(token);
        assertNotNull(allClaims);
        System.out.println(allClaims);
    }
    @Test
    void shouldExtractUsername(){
        String token = jwtUtil.createAccessToken("osho@gmail.com");
        String username = jwtUtil.extractUsername(token);
        assertNotNull(username);
        assertFalse(username.isEmpty());
        System.out.println("username is: "+username);
    }
    @Test
    void shouldValidateToken(){
        String token = jwtUtil.createAccessToken("osho@gmail.com");
        Boolean b = jwtUtil.validateToken(token, "OSho@gmail.com");
        System.out.println("token validation result with uppercase mail: "+b);
        assertTrue(b);
    }
    @Test
    void shouldRejectWrongUsername(){
        String token = jwtUtil.createAccessToken("osho@gmail.com");
        Boolean b = jwtUtil.validateToken(token, "divyanshu@gmail.com");
        System.out.println("token validation result with different mail : "+b);
        assertFalse(b);

    }
    @Test
    void shouldRejectTamperedToken(){
        String token = jwtUtil.createAccessToken("osho@gmail.com");
        String tamperedToken = token.substring(0,token.length()-2)+"XX";
        Boolean b = jwtUtil.validateToken(tamperedToken, "OSho@gmail.com");
        System.out.println("token validation result with tampered token: "+b);
        assertFalse(b);

    }

}
