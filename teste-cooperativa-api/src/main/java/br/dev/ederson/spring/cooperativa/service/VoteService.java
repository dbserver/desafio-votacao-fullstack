package br.dev.ederson.spring.cooperativa.service;

import br.dev.ederson.spring.cooperativa.model.Agenda;
import br.dev.ederson.spring.cooperativa.model.Associate;
import br.dev.ederson.spring.cooperativa.model.Vote;
import br.dev.ederson.spring.cooperativa.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VoteService {
    @Autowired
    private VoteRepository voteRepository;

    public void registerVote(Associate associate, Agenda agenda, boolean approve) {
        Vote vote = new Vote();

        vote.setAssociate(associate);
        vote.setAgenda(agenda);
        vote.setApprove(approve);
        vote.setVoteDate(LocalDateTime.now());

        voteRepository.save(vote);
    }

    public List<Agenda> getAgendasByAssociate(Associate associate) {
        List<Vote> votesByAssociate = voteRepository.findByAssociate(associate);
        return votesByAssociate.stream().map(Vote::getAgenda).collect(Collectors.toList());
    }

    public int countVotesForAgenda(Agenda agenda) {
        return voteRepository.countVotesForAgenda(agenda);
    }

    public int countVotesNotApproved(Agenda agenda) {
        return voteRepository.countVotesNotApproved(agenda);
    }

    public Vote getVoteStatus(Long agendaId, Long associateId) {
        return voteRepository.getVoteStatus(agendaId, associateId);
    }
}
