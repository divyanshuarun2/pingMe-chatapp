package com.pingme.chatapp.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "UserDB")
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private String id;
    private String name;
    private String phoneNumber;
    private String email;
    private String password;


    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
