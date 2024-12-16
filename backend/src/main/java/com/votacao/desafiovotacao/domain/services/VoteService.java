package com.votacao.desafiovotacao.domain.services;

import com.votacao.desafiovotacao.application.dtos.VoteResponseDTO;
import com.votacao.desafiovotacao.domain.entities.Session;
import com.votacao.desafiovotacao.domain.entities.Vote;
import com.votacao.desafiovotacao.domain.exceptions.AlreadyVotedException;
import com.votacao.desafiovotacao.domain.exceptions.NoSessionToVoteException;
import com.votacao.desafiovotacao.domain.exceptions.NoVoteException;
import com.votacao.desafiovotacao.infra.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.votacao.desafiovotacao.application.dtos.VoteDTO;

import java.util.List;
import java.util.Optional;


@Service
public class VoteService {

    private static final String VOTE_YES = "SIM";

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private SessionService sessionService;

    public Optional<Vote> registerVote(VoteDTO voteDTO)
            throws AlreadyVotedException, NoSessionToVoteException {

        if (validateAssociateAlreadyVote(sessionService.get(voteDTO.sessionId()), voteDTO.associatedId())) {
            throw new AlreadyVotedException();
        }

        if (validateSessionForVoting(voteDTO)) {
            throw new NoSessionToVoteException();
        }

        if (voteDTO.vote().isEmpty() || voteDTO.associatedId().isEmpty()
                || voteDTO.agendaId().isEmpty() || voteDTO.sessionId().isEmpty()
                || voteDTO.voteId().isEmpty()) {
            throw new NoVoteException();
        }

        Vote vote = Vote.builder()
                .id(voteDTO.voteId())
                .vote(associatedVoteHandler(voteDTO.vote()))
                .associatedId(voteDTO.associatedId())
                .session(sessionService.get(voteDTO.sessionId()).get())
                .build();
        return Optional.of(voteRepository.save(vote));
    }

    public Vote get(String id) {
        return voteRepository.findById(id).orElse(null);
    }

    public List<Vote> findAll() {
        return voteRepository.findAll();
    }

    public void delete(String id) {
        voteRepository.deleteById(id);
    }

    public void deleteAll() {
        voteRepository.deleteAll();
    }

    public ResponseEntity<VoteResponseDTO> getVotesResult(String sessionId) {

        List<Vote> votes = voteRepository.findAll();
        List<Vote> selectedVotes = votes.stream()
                .filter(e -> e.getSession().getId().equals(sessionId))
                .toList();

        Long totalVotesYes = 0L;
        Long totalVotesNo = 0L;
        Long totalVotes = 0L;

        for (Vote vote : selectedVotes) {
            if (vote.getVote().equals(VOTE_YES)) {
                totalVotesYes++;
            } else {
                totalVotesNo++;
            }
            totalVotes++;
        }
        VoteResponseDTO voteResponseDTO = new VoteResponseDTO(String.valueOf(totalVotesYes),
                String.valueOf(totalVotesNo), String.valueOf(totalVotes));

        return new ResponseEntity<>(voteResponseDTO, HttpStatus.OK);
    }

    public Boolean validateSessionForVoting(VoteDTO voteDTO) {

        Optional<Session> session = sessionService.get(voteDTO.sessionId());

        return (!session.isPresent()) || session.get().getStatus().equals(Session.StatusSession.ENCERRADA);
    }


    public Boolean validateAssociateAlreadyVote(Optional<Session> session, String associateId) {
        List<Vote> listVoteEntity = voteRepository.findAllBySession(session);

        return listVoteEntity
                .stream()
                .anyMatch(entity ->
                        entity.getAssociatedId().equals(associateId)
                );
    }

    private String associatedVoteHandler(String associatedVote) {
        return associatedVote.toUpperCase().trim();
    }

    public List<Vote> findAllVotesBySessionById(String sessionId) {
        return voteRepository.findAllBySessionId(sessionId);
    }
}
