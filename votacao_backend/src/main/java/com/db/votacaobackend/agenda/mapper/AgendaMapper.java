package com.db.votacaobackend.agenda.mapper;

import com.db.votacaobackend.agenda.dto.AgendaDTO;
import com.db.votacaobackend.agenda.model.Agenda;
import org.springframework.stereotype.Component;

@Component
public class AgendaMapper {

  public Agenda toEntity(AgendaDTO agendaDto) {
    return Agenda.builder()
        .name(agendaDto.name())
        .build();
  }

  public AgendaDTO toDto(Agenda agenda) {
    return new AgendaDTO(
        agenda.getId(),
        agenda.getName()
    );
  }
}
