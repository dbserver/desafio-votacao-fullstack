package com.db.votacao.api.service;

import com.db.votacao.api.model.Agenda;
import com.db.votacao.api.model.Vote;
import com.db.votacao.api.model.VotingResult;
import com.db.votacao.api.model.VotingSession;

import java.util.List;

public interface VotingService {

    Agenda createAgenda(String title, String description);

    VotingSession openVotingSession(Long agendaId, Integer durationMinutes);

    Vote registerVote(Long sessionId, String associateId, boolean voteValue);

    VotingResult getVotingResult(Long sessionId);
    List<Agenda> getAllAgendas();

    List<VotingSession> getAllSessions();
}
