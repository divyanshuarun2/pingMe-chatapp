package com.pingme.chatapp.service;

import com.pingme.chatapp.entity.SessionEntity;

public interface SessionService {
    void saveSession(SessionEntity session);
    SessionEntity getSession(String email);

    Boolean deleteSessionEntry(String userId);
}
