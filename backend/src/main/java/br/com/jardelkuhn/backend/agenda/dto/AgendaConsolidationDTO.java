package br.com.jardelkuhn.backend.agenda.dto;

import br.com.jardelkuhn.backend.agenda.entity.Agenda;
import br.com.jardelkuhn.backend.agenda.entity.AgendaStatus;
import br.com.jardelkuhn.backend.agenda.entity.AgendaVotingProjection;
import br.com.jardelkuhn.backend.agenda.businessrule.AgendaStatusCalculator;

import java.time.LocalDateTime;

public record AgendaConsolidationDTO(
        Long id,
        String title,
        String description,
        AgendaStatus status,
        LocalDateTime limitDate,
        long yesVotes,
        long noVotes,
        long totalVotes
) {
    public static AgendaConsolidationDTO fromNewAgenda(Agenda agenda) {
        return new AgendaConsolidationDTO(
                agenda.getId(),
                agenda.getTitle(),
                agenda.getDescription(),
                AgendaStatusCalculator.calculate(agenda.getOpenedDate(), agenda.getLimitDate()),
                agenda.getLimitDate(),
                0,
                0,
                0
        );
    }

    public static AgendaConsolidationDTO fromProjection(AgendaVotingProjection projection) {
        return new AgendaConsolidationDTO(
                projection.id(),
                projection.title(),
                projection.description(),
                AgendaStatusCalculator.calculate(projection.openedDate(), projection.limitDate()),
                projection.limitDate(),
                projection.yesVotes(),
                projection.noVotes(),
                projection.totalVotes()
        );
    }
}