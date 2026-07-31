package br.com.jardelkuhn.backend.service;

import br.com.jardelkuhn.backend.dto.*;
import br.com.jardelkuhn.backend.entity.Agenda;
import br.com.jardelkuhn.backend.repository.AgendaRepository;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AgendaService {
    private final AgendaRepository agendaRepository;

    public AgendaService(AgendaRepository agendaRepository) {
        this.agendaRepository = agendaRepository;
    }

    public List<Agenda> listAllAgendas() {
        return agendaRepository.findAll();
    }

    public Agenda createAgenda(CreateAgendaDTO createAgendaDTO) {
        Agenda agenda = new Agenda();

        agenda.setTitle(createAgendaDTO.title());
        agenda.setDescription(createAgendaDTO.description());
        agenda.setOpenedDate(LocalDateTime.now());

        return agendaRepository.save(agenda);
    }

    public SessionDTO openSession(Long id, OpenSessionDTO openSessionDTO) {
        Agenda agenda = agendaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(
                "Agenda with id " + id + " not found"));

        if (agenda.getOpenedDate() != null) {
            throw new AgendaAlreadyOpenedException();
        }

        if (openSessionDTO.duration() < 1){
            throw new AgendaDurationInvalidException();
        }

        LocalDateTime openedDate = LocalDateTime.now();
        Long duration = openSessionDTO.duration();

        LocalDateTime sessionLimitDate = openedDate.plusMinutes(duration);

        agenda.setOpenedDate(openedDate);
        agenda.setLimitDate(sessionLimitDate);

        agendaRepository.save(agenda);

        return new SessionDTO(agenda.getLimitDate());
    }
}