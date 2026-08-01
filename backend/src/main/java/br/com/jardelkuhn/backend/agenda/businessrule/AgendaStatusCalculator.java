package br.com.jardelkuhn.backend.agenda.businessrule;

import br.com.jardelkuhn.backend.agenda.entity.AgendaStatus;

import java.time.LocalDateTime;

public class AgendaStatusCalculator {
    public static AgendaStatus calculate(LocalDateTime openedDate, LocalDateTime limitDate) {
        if (openedDate == null || limitDate == null) {
            return AgendaStatus.PENDING;
        }

        if (LocalDateTime.now().isAfter(limitDate)) {
            return AgendaStatus.FINISHED;
        }

        return AgendaStatus.STARTED;
    }
}
