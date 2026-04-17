package com.pingme.chatapp.dto;

public class LoginResponseDto {
    private String refreshToken;
    private String token;
    private String userid;
    private String loginMessage;

    public LoginResponseDto(String token, String userid, String loginMessage, String refreshToken) {
        this.token = token;
        this.userid = userid;
        this.loginMessage = loginMessage;
        this.refreshToken=refreshToken;
    }

    public String getToken() {
        return token;
    }

    public String getUserid() {
        return userid;
    }

    public String getLoginMessage() {
        return loginMessage;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}
