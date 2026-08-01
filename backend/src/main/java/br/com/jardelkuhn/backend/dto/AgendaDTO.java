package br.com.jardelkuhn.backend.dto;

import br.com.jardelkuhn.backend.entity.Agenda;
import br.com.jardelkuhn.backend.entity.AgendaStatus;

import java.time.LocalDateTime;

public record AgendaDTO(
        Long id,
        String title,
        String description,
        LocalDateTime openedDate,
        LocalDateTime limitDate,
        AgendaStatus status
) {

    public AgendaDTO(Agenda agenda) {
        this(
                agenda.getId(),
                agenda.getTitle(),
                agenda.getDescription(),
                agenda.getOpenedDate(),
                agenda.getLimitDate(),
                agenda.getStatus()
        );
    }
}