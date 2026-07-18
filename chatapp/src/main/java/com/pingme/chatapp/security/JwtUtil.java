package com.pingme.chatapp.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {
    @Value("${jwt.expiration}")
    private long jwtExpiry;

    @Value("${jwt.secret}")
    private String jwtSecret;

    private SecretKey getSecretKey(){
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    public String createAccessToken(String email){
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+jwtExpiry))
                .signWith(getSecretKey())
                .compact();
    }
    /*why this function is wrong? --> because we are just decoding the payload not doing signature verification
    public String decodePayload(String token){
        String[] split = token.split("\\.");
        byte[] decode = Base64.getDecoder().decode(split[1]);
        String claims = new String(decode);
        return claims;

    }
    */
     public Claims getAllClaims(String token){
         Claims claims = null;

             claims = Jwts.parser()
                    .verifyWith(getSecretKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

return claims;
     }
     public String extractUsername(String token){
         return getAllClaims(token).getSubject();
         // we can also extrain any value from a map.get("key");
     }
     private Boolean isTokenExpired(String token){
       long expTime = getAllClaims(token).getExpiration().getTime();
         long currTime = System.currentTimeMillis();
         return currTime>expTime;


     }
     public Boolean validateToken(String token, String username){
         try{
             System.out.println(getAllClaims(token).getSubject().equalsIgnoreCase(username));
             System.out.println(!isTokenExpired(token));
             return
                     (getAllClaims(token).getSubject().equalsIgnoreCase(username)) && !isTokenExpired(token);
         }
         catch (JwtException e){
             System.out.println(e.getMessage());
             return false;
         }

     }

}
