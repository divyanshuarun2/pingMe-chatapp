package com.pingme.chatapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
@Table(name="user_session")
public class SessionEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @Email
    @Column(unique = true,nullable=false)
    private String email;
    @Column(unique = true,nullable=false)
    private String jsessionId;
    @DateTimeFormat
    private LocalDateTime timeFormat;

    public SessionEntity(String email, String jsessionId) {
        this.email = email;
        this.jsessionId = jsessionId;
    }
    public SessionEntity(){}

    public String getEmail() {
        return email;
    }

    public String getJsessionId() {
        return jsessionId;
    }

    public LocalDateTime getTimeFormat() {
        return timeFormat;
    }

    public void setTimeFormat(LocalDateTime timeFormat) {
        this.timeFormat = timeFormat;
    }

    public void setJsessionId(String jsessionId) {
        this.jsessionId = jsessionId;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
