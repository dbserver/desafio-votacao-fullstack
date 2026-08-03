package br.com.jardelkuhn.backend.agenda.businessrule;

import br.com.jardelkuhn.backend.agenda.entity.AgendaStatus;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDateTime;

@Component
public class AgendaStatusCalculator {

    private final Clock clock;

    public AgendaStatusCalculator(Clock clock) {
        this.clock = clock;
    }

    public AgendaStatus calculate(
            LocalDateTime openedDate,
            LocalDateTime limitDate
    ) {
        if (openedDate == null || limitDate == null) {
            return AgendaStatus.PENDING;
        }

        if (LocalDateTime.now(clock).isAfter(limitDate)) {
            return AgendaStatus.FINISHED;
        }

        return AgendaStatus.STARTED;
    }
}