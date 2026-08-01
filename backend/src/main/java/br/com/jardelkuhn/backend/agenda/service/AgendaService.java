package br.com.jardelkuhn.backend.agenda.service;

import br.com.jardelkuhn.backend.agenda.dto.*;
import br.com.jardelkuhn.backend.agenda.exception.*;
import br.com.jardelkuhn.backend.agenda.entity.Agenda;
import br.com.jardelkuhn.backend.agenda.entity.AgendaVotingProjection;
import br.com.jardelkuhn.backend.agenda.entity.Vote;
import br.com.jardelkuhn.backend.agenda.repository.AgendaRepository;

import br.com.jardelkuhn.backend.agenda.repository.VoteRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AgendaService {
    private final AgendaRepository agendaRepository;
    private final VoteRepository voteRepository;

    public AgendaService(AgendaRepository agendaRepository, VoteRepository voteRepository) {
        this.agendaRepository = agendaRepository;
        this.voteRepository = voteRepository;
    }

    public List<AgendaVotingProjection> findAllAgendaVotingProjection() {
        return agendaRepository.findAllAgendaVotingProjection();
    }

    public Agenda createAgenda(CreateAgendaDTO createAgendaDTO) {
        Agenda agenda = new Agenda();

        agenda.setTitle(createAgendaDTO.title());
        agenda.setDescription(createAgendaDTO.description());

        return agendaRepository.save(agenda);
    }

    public LocalDateTime openAgendaAndReturnLimitDate(Long agendaId, Integer duration) {
        Agenda agenda = agendaRepository.findById(agendaId).orElseThrow(() -> new EntityNotFoundException(
                "Agenda com id " + agendaId + " não encontrada"));

        boolean isAgendaAlreadyOpened = agenda.getOpenedDate() != null;

        if (isAgendaAlreadyOpened) {
            throw new AlreadyOpenedException();
        }

        if (duration < 1) {
            throw new DurationInvalidException();
        }

        LocalDateTime openedDate = LocalDateTime.now();

        LocalDateTime sessionLimitDate = openedDate.plusMinutes(duration);

        agenda.setOpenedDate(openedDate);
        agenda.setLimitDate(sessionLimitDate);

        agendaRepository.save(agenda);

        return agenda.getLimitDate();
    }

    public void voteOnAgenda(Long agendaId, VoteDTO voteDTO) {
        Agenda agenda = agendaRepository.findById(agendaId).orElseThrow(() -> new EntityNotFoundException(
                "Agenda com id " + agendaId + " não encontrada"));

        boolean isVotingSessionNotOpened = agenda.getOpenedDate() == null || LocalDateTime.now().isBefore(agenda.getOpenedDate()) || agenda.getLimitDate() == null;

        if (isVotingSessionNotOpened) {
            throw new NotStartedException();
        }

        boolean isVotingSessionClosed = agenda.getLimitDate().isBefore(LocalDateTime.now());

        if (isVotingSessionClosed) {
            throw new AlreadyClosedException();
        }

        boolean invalidAssociateId = voteDTO.associateId() == null || voteDTO.associateId().isBlank();

        if (invalidAssociateId) {
            throw new InvalidAssociateException();
        }

        boolean invalidVoteOption = voteDTO.option() == null;

        if (invalidVoteOption) {
            throw new InvalidVoteOptionException();
        }

        Optional<Vote> associateAlreadyVoted = voteRepository.findByAgendaAndAssociateId(agenda, voteDTO.associateId());

        if (associateAlreadyVoted.isPresent()) {
            throw new DuplicatedVoteException();
        }

        Vote vote = new Vote();
        vote.setAgenda(agenda);
        vote.setAssociateId(voteDTO.associateId());
        vote.setVoteOption(voteDTO.option());

        voteRepository.save(vote);
    }
}