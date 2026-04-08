package com.pingme.chatapp.service;

import com.pingme.chatapp.entity.SessionEntity;

public interface SessionService {
    void saveSession(SessionEntity session);
    String getSession(String userId);

}
