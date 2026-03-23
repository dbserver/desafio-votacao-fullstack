package br.tec.db.voting_api.dto.output;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Timer;

public record AgendaDetailOutputDTO(
        Long id,
        String title,
        String description,
        Boolean createdSession,
        Boolean sessionOpen,
        String timer,
        LocalDateTime createdAt
) { }
