package com.votacao.desafiovotacao.domain.services;

import com.votacao.desafiovotacao.application.dtos.SessionDTO;
import com.votacao.desafiovotacao.domain.entities.Agenda;
import com.votacao.desafiovotacao.domain.entities.Session;
import com.votacao.desafiovotacao.domain.exceptions.AgendaNotFoundException;
import com.votacao.desafiovotacao.domain.exceptions.SessionTimeException;
import com.votacao.desafiovotacao.infra.AgendaRepository;
import com.votacao.desafiovotacao.infra.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
public class SessionService {

    private static final Long TIME_DEFAULT = 1L;

    @Autowired
    SessionRepository sessionRepository;
    @Autowired
    private AgendaRepository agendaRepository;

    public Session createSession(SessionDTO sessionDTO)
            throws AgendaNotFoundException, SessionTimeException {

        long duration;
        if (!sessionDTO.duration().isEmpty()) {
            try {
                duration = Long.parseLong(sessionDTO.duration());
            } catch (NumberFormatException e) {
                throw new NumberFormatException("The duration must be a integer");
            }
        } else {
            duration = TIME_DEFAULT;
        }

        String agendaId = sessionDTO.agendaId();

        validatePostSessionRequest(sessionDTO, duration);

        Session session = Session.builder()
                .id(sessionDTO.id())
                .agendaId(agendaId)
                .startTime(LocalDateTime.now())
                .closedTime(LocalDateTime.now().plusMinutes(duration))
                .status(Session.StatusSession.ABERTA)
                .build();
        return sessionRepository.save(session);
    }

    public Optional<Session> get(String sessionId) {

        Session session = sessionRepository.findById(sessionId).get();

        if (isSessionClosed(session)) {
            closeSession(session);
        }
        return Optional.of(session);
    }

    public List<Session> findAll() {

        List<Session> sessions = sessionRepository.findAll();
        for (Session s : sessions) {
            if (isSessionClosed(s)) {
                closeSession(s);
            }
        }
        return sessions;
    }

    public Session update(String id, SessionDTO sessionDTO) {
        Session session = sessionRepository.findById(id).orElse(null);
        if (session == null) return null;
        session.setAgendaId(sessionDTO.agendaId());
        session.setStartTime(LocalDateTime.now());
        session.setClosedTime(LocalDateTime.now().plusMinutes(Long.parseLong(sessionDTO.duration())));
        session.setStatus(Session.StatusSession.ABERTA);
        return sessionRepository.save(session);
    }

    public void delete(String sessionId) {
        sessionRepository.deleteById(sessionId);
    }

    public void deleteAll() {
        sessionRepository.deleteAll();
    }

    public boolean isSessionClosed(Session session) {
        return LocalDateTime.now().isAfter(session.getClosedTime());
    }

    public void closeSession(Session session) {
        session.setStatus(Session.StatusSession.ENCERRADA);
        sessionRepository.save(session);
    }

    public void validatePostSessionRequest(SessionDTO sessionDTO, Long minutesOpened)
            throws AgendaNotFoundException, SessionTimeException {

        Optional<Agenda> optionalAgenda = agendaRepository.findById(sessionDTO.agendaId());

        if (optionalAgenda.isEmpty()) {
            throw new AgendaNotFoundException();
        }

        if (minutesOpened < TIME_DEFAULT) {
            throw new SessionTimeException();
        }
    }
}
