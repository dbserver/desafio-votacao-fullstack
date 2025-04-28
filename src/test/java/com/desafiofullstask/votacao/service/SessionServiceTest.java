package com.desafiofullstask.votacao.service;

import com.desafiofullstask.votacao.entity.Session;
import com.desafiofullstask.votacao.repository.SessionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class SessionServiceTest {

    @Autowired
    private SessionService sessionService;

    @Autowired
    private SessionRepository sessionRepository;

    private Session session;

    @BeforeEach
    void setup() {
        session = new Session();
        session.setDuration(5);
        session.setStatus(1);
        session.setCreatedTime(Timestamp.valueOf(LocalDateTime.now()));
        sessionRepository.save(session);


        List<Session> sessions = sessionRepository.findSessionsToChangeStatus(Timestamp.valueOf(LocalDateTime.now()));
        assertThat(sessions).isNotEmpty();
        session = sessions.get(0); // Pegando o primeiro
    }

    @Test
    void findSessionById() {
        assertThat(sessionRepository.findById(session.getId()).isPresent()).isTrue();
    }


    @Test
    void delete() {

        sessionRepository.deleteById(session.getId());

        assertThat(sessionRepository.findById(session.getId()).isPresent()).isFalse();
    }
}