package br.dev.ederson.spring.cooperativa.service;

import br.dev.ederson.spring.cooperativa.enumerador.AgendaStatus;
import br.dev.ederson.spring.cooperativa.exception.BadRequestException;
import br.dev.ederson.spring.cooperativa.exception.NotFoundException;
import br.dev.ederson.spring.cooperativa.model.Agenda;
import br.dev.ederson.spring.cooperativa.model.Session;
import br.dev.ederson.spring.cooperativa.repository.SessionRepository;
import br.dev.ederson.spring.cooperativa.util.ProjectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class SessionService {
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private VoteService voteService;
    @Autowired
    private AgendaService agendaService;

    public List<Session> getSessions() {
        return sessionRepository.findAll();
    }

    public List<Session> getAllSessionsNotInitialized() {
        return sessionRepository.getAllSessionsNotInitialized();
    }

    public Session getById(Long agendaId) {
        return sessionRepository.findById(agendaId).orElse(null);
    }

    public Session createSession(Session session) {
        if (session.getDurationTime() <= 0)
            session.setDurationTime(1);

        return sessionRepository.save(session);
    }

    public Session startSession(Long sessionId) throws BadRequestException, NotFoundException {
        Session session = sessionRepository.findById(sessionId).orElse(null);

        if (session == null)
            throw new NotFoundException("Session " + sessionId + " not found!");

        if (ProjectUtil.isEmpty(agendaService.getAgendasBySession(session)))
            throw new BadRequestException("Session Agendas must be specified before start the vote process!");

        if (session.getStartDate() != null)
            throw new BadRequestException("Session is already started!");

        session.setStartDate(LocalDateTime.now());
        setAgendasInVotation(session);
        session = sessionRepository.save(session);
        System.out.println("Iniciando sessão " + session.getId() + ".\nData: " + LocalDateTime.now());
        initializeTimer(session);
        return session;
    }

    private void setAgendasInVotation(Session session) {
        agendaService.getAgendasBySession(session).forEach(agenda -> agenda.setStatus(AgendaStatus.VOTING));
    }

    private void initializeTimer(Session session) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                endSession(session);
            }
        }, (session.getDurationTime() * 1000L * 60L));
    }

    private void endSession(Session session) {
        session.setEndDate(LocalDateTime.now());
        System.out.println("Encerrando sessão " + session.getId() + ".\nData: " + LocalDateTime.now());
        computeVotes(session);
        sessionRepository.save(session);
    }

    private void computeVotes(Session session) {
        List<Agenda> agendasBySession = agendaService.getAgendasBySession(session);
        ExecutorService executorService = Executors.newFixedThreadPool(agendasBySession.size());

        agendasBySession.forEach(agenda -> executorService.execute(() -> {
            int qtdVotes = voteService.countVotesForAgenda(agenda);
            int qtdVotesNotApproved = voteService.countVotesNotApproved(agenda);

            if ((qtdVotes == 0) || (qtdVotesNotApproved > (qtdVotes / 2)))
                agenda.setStatus(AgendaStatus.REJECTED);
            else
                agenda.setStatus(AgendaStatus.APPROVED);

            agendaService.save(agenda);
        }));
    }

    public boolean isSessionOpenToVote(Session session) {
        //Controle implementado assim para ter mais exatidão
        LocalDateTime endDate = session.getStartDate().plusMinutes(session.getDurationTime());
        return LocalDateTime.now().isBefore(endDate);
    }
}
