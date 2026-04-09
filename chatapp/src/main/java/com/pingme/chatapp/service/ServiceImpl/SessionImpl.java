package com.pingme.chatapp.service.ServiceImpl;

import com.pingme.chatapp.entity.SessionEntity;
import com.pingme.chatapp.repository.SessionRepository;
import com.pingme.chatapp.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class SessionImpl implements SessionService {

    @Autowired
    SessionRepository sessionRepository;

    @Override
    //@Transactional
    public void saveSession(SessionEntity currentSession) {
        SessionEntity existingSession = sessionRepository.findByEmail(currentSession.getEmail());
        if(existingSession!=null) {
         //update existing session
            existingSession.setJsessionId(currentSession.getJsessionId());
            existingSession.setTimeFormat(LocalDateTime.now());
            sessionRepository.save(existingSession);
            return;
     }
     //  new
        currentSession.setTimeFormat(LocalDateTime.now());
        SessionEntity saveNewSession = sessionRepository.save(currentSession);

    }

    @Override
    public SessionEntity getSession(String email) {
        return sessionRepository.findByEmail(email);

    }

    @Override
    @Transactional
    public Boolean deleteSessionEntry(String userId) {
        try {
            sessionRepository.deleteByEmail(userId);

        }
        catch (Exception e){
                throw new RuntimeException(e);
            }
        return true;

    }
}
