package com.sarapio.votacao_api.service;

import com.sarapio.votacao_api.domain.session.Session;
import com.sarapio.votacao_api.domain.session.SessionRequestDTO;
import com.sarapio.votacao_api.domain.topic.Topic;
import com.sarapio.votacao_api.repositories.SessionRepository;
import com.sarapio.votacao_api.repositories.TopicRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class SessionService {

    private final SessionRepository sessionRepository;
    private final TopicRepository topicRepository;

    public SessionService(SessionRepository sessionRepository, TopicRepository topicRepository) {
        this.sessionRepository = sessionRepository;
        this.topicRepository = topicRepository;
    }

    public Session createSession(UUID pautaId, SessionRequestDTO sessaoData) {
        Topic topic = topicRepository.findById(pautaId).orElseThrow(() -> new IllegalArgumentException("Topic not found"));

        LocalDate dataFinal = LocalDate.now().plusDays(1);
        Date dateEnd = Date.from(dataFinal.atStartOfDay(ZoneId.systemDefault()).toInstant());

        Session session = new Session();
        session.setDateEnd(dateEnd);
        session.setDateStart(new Date(sessaoData.dateStart()));
        session.setTopic(topic);

        return sessionRepository.save(session);
    }

    public List<Session> listSession() {
        return sessionRepository.findAll();
    }
}
