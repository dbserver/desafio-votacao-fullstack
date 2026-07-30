package br.com.jardelkuhn.backend.service;

import br.com.jardelkuhn.backend.dto.AgendaAlreadyOpenedException;
import br.com.jardelkuhn.backend.dto.CreateAgendaDTO;
import br.com.jardelkuhn.backend.dto.OpenSessionDTO;
import br.com.jardelkuhn.backend.dto.SessionDTO;
import br.com.jardelkuhn.backend.entity.Agenda;
import br.com.jardelkuhn.backend.repository.AgendaRepository;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Sort;
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
        return agendaRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    public Agenda createAgenda(CreateAgendaDTO createAgendaDTO) {
        Agenda agenda = new Agenda();

        agenda.setTitle(createAgendaDTO.title());
        agenda.setDescription(createAgendaDTO.description());
        agenda.setOpenedDate(LocalDateTime.now());

        return agendaRepository.save(agenda);
    }

}