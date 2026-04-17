package com.pingme.chatapp.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String jwtSecretString;

    @Value("${jwt.expiration}")
    private long jwtExpirations;

    @Value("${jwt.refresh.expiration}")
    private long jwtRefreshExpiration;

    //get signing key from secret string
    private SecretKey getSecretKey(String jwtSecretString){

        //1. convert into byte array
        byte[] bytes= jwtSecretString.getBytes(StandardCharsets.UTF_8);
        //2. byte array to  Secret key
        return Keys.hmacShaKeyFor(bytes);

    }

    //generate token for given username
    public String generateToken(String email){
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirations))
                .signWith(getSecretKey(jwtSecretString))
                .compact();
    }
    public String generateRefreshToken(String email){
        return Jwts.builder()
                .signWith(getSecretKey(jwtSecretString))
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+jwtRefreshExpiration))
                .claim("tokenType","refresh")
                .compact();
    }
    private Claims getAllClaims(String token){
        Jwt parse = Jwts.parserBuilder()
                .setSigningKey(getSecretKey(jwtSecretString)).build()
                .parseClaimsJws(token);
        System.out.println(parse.toString());
        return (Claims) parse.getBody();


    }
    public Date getExpiry(String token){
        return getAllClaims(token).getExpiration();
    }
    public String extractUsername(String token){
        return getAllClaims(token).getSubject();
    }

    public Boolean isTokenExpired(String token){

        Date expiration = getAllClaims(token).getExpiration();
        long expirationTimeMs= expiration.getTime();
        long systemTime= System.currentTimeMillis();
        return systemTime > expirationTimeMs;
    }
    public Boolean validateToken(String token, String email){
        try{
            return extractUsername(token).equals(email) && !isTokenExpired(token);
        }
        catch (ExpiredJwtException e){
            System.out.println("token is expired :"+e.getMessage()) ;
            return false;
        }
        catch (JwtException e){
            System.out.println("invalid jwt token :"+e.getMessage());
            return false;
        }
    }
}
