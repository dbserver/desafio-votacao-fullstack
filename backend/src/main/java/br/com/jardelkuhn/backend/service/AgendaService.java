package br.com.jardelkuhn.backend.service;

import br.com.jardelkuhn.backend.dto.AgendaAlreadyOpenedException;
import br.com.jardelkuhn.backend.dto.AgendaDurationInvalidException;
import br.com.jardelkuhn.backend.dto.CreateAgendaDTO;
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

        return agendaRepository.save(agenda);
    }

    public LocalDateTime openSessionAndReturnLimitDate(Long id, Integer duration) {
        Agenda agenda = agendaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(
                "Agenda com id " + id + " não encontrada"));

        if (agenda.getOpenedDate() != null) {
            throw new AgendaAlreadyOpenedException();
        }

        if (duration < 1) {
            throw new AgendaDurationInvalidException();
        }

        LocalDateTime openedDate = LocalDateTime.now();

        LocalDateTime sessionLimitDate = openedDate.plusMinutes(duration);

        agenda.setOpenedDate(openedDate);
        agenda.setLimitDate(sessionLimitDate);

        agendaRepository.save(agenda);

        return agenda.getLimitDate();
    }
}