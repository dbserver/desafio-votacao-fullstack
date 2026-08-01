package br.com.jardelkuhn.backend.agenda.entity;

import java.time.LocalDateTime;

public record AgendaVotingProjection(
        Long id,
        String title,
        String description,
        LocalDateTime openedDate,
        LocalDateTime limitDate,
        long yesVotes,
        long noVotes,
        long totalVotes
) {
}