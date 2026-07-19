package com.pingme.chatapp.dto;

public class LoginResponseDto {
    private String accessToken;
    private String email;
    private String message;

    public LoginResponseDto(String accessToken, String email, String message) {
        this.accessToken = accessToken;
        this.email = email;
        this.message = message;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getEmail() {
        return email;
    }

    public String getMessage() {
        return message;
    }
}
