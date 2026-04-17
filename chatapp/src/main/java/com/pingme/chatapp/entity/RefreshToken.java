package com.pingme.chatapp.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String refreshToken;
    private String username;
    private Date expireAt;
    private boolean isRevoked;
    private Date createdAt;

    public RefreshToken(String refreshToken, String username, Date expires_at, boolean is_revoked, Date created_at) {
        this.refreshToken = refreshToken;
        this.username = username;
        this.expireAt = expires_at;
        this.isRevoked = is_revoked;
        this.createdAt = created_at;
    }
    // JPA requires a no-args constructor to work
// Without it → JPA cannot create instances when loading from DB
// Add this:
    public RefreshToken() { }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getUsername() {
        return username;
    }

    public Date getExpires_at() {
        return expireAt;
    }

    public boolean isRevoked() {
        return isRevoked;
    }

    public Date getCreated_at() {
        return createdAt;
    }
}
