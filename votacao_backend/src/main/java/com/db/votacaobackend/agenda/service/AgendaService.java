package com.db.votacaobackend.agenda.service;

import com.db.votacaobackend.agenda.exception.AgendaBadRequestException;
import com.db.votacaobackend.agenda.exception.AgendaNotFoundExcepcion;
import com.db.votacaobackend.agenda.model.Agenda;
import com.db.votacaobackend.agenda.repository.AgendaRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AgendaService {

  private final AgendaRepository repository;

  public Agenda createAgenda(Agenda agenda) {
    if (agenda.getName() == null) {
      throw new AgendaBadRequestException("O nome da pauta é obrigatório");
    }
    return repository.save(agenda);
  }

  public List<Agenda> listAllAgendas() {
    return repository.findAll();
  }

  public Agenda getById(Long id) throws AgendaNotFoundExcepcion {
    return repository.findById(id)
        .orElseThrow(() -> new AgendaNotFoundExcepcion("Pauta não encontrada!"));
  }
}