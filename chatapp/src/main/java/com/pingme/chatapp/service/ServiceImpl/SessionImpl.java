package com.pingme.chatapp.service.ServiceImpl;

import com.pingme.chatapp.entity.SessionEntity;
import com.pingme.chatapp.repository.SessionRepository;
import com.pingme.chatapp.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SessionImpl implements SessionService {

    @Autowired
    SessionRepository sessionRepository;

    @Override
    public void saveSession(SessionEntity session) {
        session.setTimeFormat(LocalDateTime.now());
        sessionRepository.save(session);

    }

    @Override
    public String getSession(String userId) {
        SessionEntity userSession = sessionRepository.findByEmail(userId);
        if(userSession!=null){
            return userSession.getJsessionId();
        }
        return null;

    }
}
