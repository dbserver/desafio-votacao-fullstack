package com.db.votacao.api.service;

import com.db.votacao.api.exception.BusinessException;
import com.db.votacao.api.exception.ResourceNotFoundException;
import com.db.votacao.api.facade.CpfValidatorFacade;
import com.db.votacao.api.model.Agenda;
import com.db.votacao.api.model.Vote;
import com.db.votacao.api.model.VotingResult;
import com.db.votacao.api.model.VotingSession;
import com.db.votacao.api.repository.AgendaRepository;
import com.db.votacao.api.repository.VoteRepository;
import com.db.votacao.api.repository.VotingSessionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class VotingServiceImpl implements VotingService {
    private final AgendaRepository agendaRepository;
    private final VotingSessionRepository sessionRepository;
    private final VoteRepository voteRepository;
    private final RedisService redisService;
    private final BatchVoteProcessor batchVoteProcessor;
    private final VotingResultService votingResultService;
    private final CpfValidatorFacade cpfValidatorFacade;

    private static final int BATCH_SIZE = 1000;
    private final List<Vote> voteBatch = new CopyOnWriteArrayList<>();

    public VotingServiceImpl(AgendaRepository agendaRepository, VotingSessionRepository sessionRepository, VoteRepository voteRepository, RedisService redisService, BatchVoteProcessor batchVoteProcessor, VotingResultService votingResultService, CpfValidatorFacade cpfValidatorFacade) {
        this.agendaRepository = agendaRepository;
        this.sessionRepository = sessionRepository;
        this.voteRepository = voteRepository;
        this.redisService = redisService;
        this.batchVoteProcessor = batchVoteProcessor;
        this.votingResultService = votingResultService;
        this.cpfValidatorFacade = cpfValidatorFacade;
    }


    @Transactional
    public Agenda createAgenda(String title, String description) {
        Agenda agenda = new Agenda();
        agenda.setTitle(title);
        agenda.setDescription(description);
        return agendaRepository.save(agenda);
    }

    @Transactional
    public VotingSession openVotingSession(Long agendaId, Integer durationMinutes) {
        Agenda agenda = agendaRepository.findById(agendaId)
                .orElseThrow(() -> new ResourceNotFoundException("Agenda not found with id: " + agendaId));

        int duration = durationMinutes != null ? durationMinutes : 1;

        VotingSession session = new VotingSession();
        session.setAgenda(agenda);
        session.setStartTime(LocalDateTime.now());
        session.setEndTime(session.getStartTime().plusMinutes(duration));
        session.setOpen(true);

        return sessionRepository.save(session);
    }

    @Transactional
    public synchronized Vote registerVote(Long sessionId, String associateId, boolean voteValue) {
        cpfValidatorFacade.validateCpfForVoting(associateId);

        VotingSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Session not found with id: " + sessionId));

        if (!session.isSessionOpen()) {
            throw new BusinessException("Voting session is closed");
        }

        long lockTimeout = calculateSessionRemainingTime(session);

        if (lockTimeout <= 0) {
            throw new BusinessException("Voting session is expired");
        }

        if (!redisService.tryLockUserVote(sessionId, associateId, lockTimeout)) {
            throw new BusinessException("Associate has already voted in this session");
        }

        if (voteRepository.existsBySessionAndAssociateId(session, associateId)) {
            throw new BusinessException("Associate has already voted in this session");
        }

        redisService.incrementVoteCount(sessionId, voteValue);

        Vote vote = new Vote();
        vote.setSession(session);
        vote.setAssociateId(associateId);
        vote.setVoteValue(voteValue);

        voteBatch.add(vote);

        if (voteBatch.size() >= BATCH_SIZE) {
            processBatch();
        }

        return vote;
    }

    private long calculateSessionRemainingTime(VotingSession session) {
        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(session.getEndTime())) {
            return 0;
        }
        return ChronoUnit.SECONDS.between(now, session.getEndTime());
    }

    private void processBatch() {
        if (!voteBatch.isEmpty()) {
            List<Vote> batchToProcess = new ArrayList<>(voteBatch);
            voteBatch.clear();
            batchVoteProcessor.processBatchVotes(batchToProcess);
        }
    }

    public VotingResult getVotingResult(Long sessionId) {

        processBatch();

        return votingResultService.getVotingResult(sessionId);

    }

    @Override
    public List<Agenda> getAllAgendas() {
        return agendaRepository.findAll();
    }

    @Override
    public List<VotingSession> getAllSessions() {
        return sessionRepository.findAll();
    }

}
