package br.com.jardelkuhn.backend.rules;

import br.com.jardelkuhn.backend.entity.AgendaStatus;

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
