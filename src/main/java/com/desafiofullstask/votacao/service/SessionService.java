package com.desafiofullstask.votacao.service;

import com.desafiofullstask.votacao.entity.Session;
import com.desafiofullstask.votacao.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SessionService {

    @Autowired
    private SessionRepository sessionRepository;

    public Session save(Session session) {
        return sessionRepository.save(session);
    }


}
