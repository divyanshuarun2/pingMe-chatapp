package com.pingme.chatapp.dto;

public class LoginResponseDto {
    private String token;
    private String userid;
    private String loginMessage;

    public LoginResponseDto(String token, String userid, String loginMessage) {
        this.token = token;
        this.userid = userid;
        this.loginMessage = loginMessage;
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
}
