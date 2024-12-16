package com.votacao.desafiovotacao.domain.services;

import com.votacao.desafiovotacao.application.dtos.AgendaDTO;
import com.votacao.desafiovotacao.domain.entities.Agenda;
import com.votacao.desafiovotacao.domain.exceptions.AgendaNotValidException;
import com.votacao.desafiovotacao.infra.AgendaRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class AgendaService {

    @Autowired
    private AgendaRepository agendaRepository;

    public Agenda createAgenda(AgendaDTO agendaDTO) throws AgendaNotValidException {

        if (agendaDTO.description().isEmpty() || agendaDTO.id().isEmpty()) throw new AgendaNotValidException();

        Agenda agenda = Agenda.builder().build();
        BeanUtils.copyProperties(agendaDTO, agenda);

        if(agendaDTO.status() == null) {
            agenda.setStatus("aberta");
        }

        return agendaRepository.save(agenda);
    }

    public Agenda get(String id) {
        return agendaRepository.findById(id).orElse(null);
    }

    public List<Agenda> findAll() {
        return agendaRepository.findAll();
    }

    public Agenda update(String id, AgendaDTO agendaDTO) {
        Agenda agenda = agendaRepository.findById(id).orElse(null);
        if (agenda == null) return null;
        agenda.setTitle(agendaDTO.title());
        agenda.setDescription(agendaDTO.description());
        agenda.setStatus(agendaDTO.status());
        return agendaRepository.save(agenda);
    }

    public void delete(String id) {
        agendaRepository.deleteById(id);
    }

    public void deleteAll() {
        agendaRepository.deleteAll();
    }
}
