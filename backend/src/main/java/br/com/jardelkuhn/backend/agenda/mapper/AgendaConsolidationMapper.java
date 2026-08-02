package br.com.jardelkuhn.backend.agenda.mapper;

import br.com.jardelkuhn.backend.agenda.businessrule.AgendaStatusCalculator;
import br.com.jardelkuhn.backend.agenda.dto.AgendaConsolidationDTO;
import br.com.jardelkuhn.backend.agenda.entity.Agenda;
import br.com.jardelkuhn.backend.agenda.entity.AgendaVotingProjection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AgendaConsolidationMapper {

    @Autowired
    private final AgendaStatusCalculator statusCalculator;

    public AgendaConsolidationMapper(AgendaStatusCalculator statusCalculator) {
        this.statusCalculator = statusCalculator;
    }

    public AgendaConsolidationDTO fromProjection(AgendaVotingProjection projection) {
        return new AgendaConsolidationDTO(
                projection.id(),
                projection.title(),
                projection.description(),
                statusCalculator.calculate(
                        projection.openedDate(),
                        projection.limitDate()
                ),
                projection.limitDate(),
                projection.yesVotes(),
                projection.noVotes(),
                projection.totalVotes()
        );
    }

    public AgendaConsolidationDTO fromAgenda(Agenda agenda) {
        return new AgendaConsolidationDTO(
                agenda.getId(),
                agenda.getTitle(),
                agenda.getDescription(),
                statusCalculator.calculate(
                        agenda.getOpenedDate(),
                        agenda.getLimitDate()
                ),
                agenda.getLimitDate(),
                0,
                0,
                0
        );
    }
}
