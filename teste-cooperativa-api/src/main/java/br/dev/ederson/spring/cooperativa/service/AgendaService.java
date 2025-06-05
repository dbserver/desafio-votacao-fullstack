package br.dev.ederson.spring.cooperativa.service;

import br.dev.ederson.spring.cooperativa.enumerador.AgendaStatus;
import br.dev.ederson.spring.cooperativa.exception.BadRequestException;
import br.dev.ederson.spring.cooperativa.model.Agenda;
import br.dev.ederson.spring.cooperativa.model.Session;
import br.dev.ederson.spring.cooperativa.repository.AgendaRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AgendaService {
    @Autowired
    private AgendaRepository agendaRepository;

    public Agenda save(Agenda agenda) {
        return agendaRepository.save(agenda);
    }

    public Agenda getById(Long agendaId) {
        return agendaRepository.findById(agendaId).orElse(null);
    }

    public Agenda createAgenda(String description) throws BadRequestException {
        if (StringUtils.isBlank(description))
            throw new BadRequestException("Agenda description must be specified!");

        Agenda agenda = new Agenda();
        agenda.setDescription(description);
        agenda.setRegistrationDate(LocalDateTime.now());
        agenda.setStatus(AgendaStatus.TO_VOTE);

        return agendaRepository.save(agenda);
    }

    public List<Agenda> getAllAgendas() {
        return agendaRepository.findAll();
    }

    public List<Agenda> getAgendasBySession(Session session) {
        return agendaRepository.findBySession(session);
    }

    public void addAgendaToSession(Agenda agenda, Session session) throws BadRequestException {
        if (agenda.getSession() != null)
            throw new BadRequestException("This Agenda is already attached in one Session!");

        addAgenda(session, agenda);
    }

    private void addAgenda(Session session, Agenda agenda) {
        agenda.setSession(session);
        agendaRepository.save(agenda);
    }
}
