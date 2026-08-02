package br.com.jardelkuhn.backend.businessrule;

import br.com.jardelkuhn.backend.agenda.businessrule.AgendaStatusCalculator;
import br.com.jardelkuhn.backend.agenda.entity.AgendaStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class AgendaStatusCalculatorTest {
    private AgendaStatusCalculator calculator;

    @BeforeEach
    void setUp() {
        Clock clock = Clock.fixed(Instant.parse("2026-08-01T12:00:00Z"), ZoneOffset.UTC);

        calculator = new AgendaStatusCalculator(clock);
    }

    @Test
    @DisplayName("Should return PENDING when openedDate is null")
    void shouldReturnPendingWhenOpenedDateIsNull() {
        AgendaStatus status = calculator.calculate(null, LocalDateTime.of(2026, 8, 2, 12, 0));

        assertEquals(AgendaStatus.PENDING, status);
    }

    @Test
    @DisplayName("Should return PENDING when limitDate is null")
    void shouldReturnPendingWhenLimitDateIsNull() {
        AgendaStatus status = calculator.calculate(LocalDateTime.of(2026, 8, 1, 10, 0), null);

        assertEquals(AgendaStatus.PENDING, status);
    }

    @Test
    @DisplayName("Should return PENDING when both dates are null")
    void shouldReturnPendingWhenBothDatesAreNull() {
        AgendaStatus status = calculator.calculate(null, null);

        assertEquals(AgendaStatus.PENDING, status);
    }

    @Test
    @DisplayName("Should return STARTED when current time is before limit date")
    void shouldReturnStarted() {
        AgendaStatus status = calculator.calculate(
                LocalDateTime.of(2026, 8, 1, 10, 0),
                LocalDateTime.of(2026, 8, 1, 13, 0)
        );

        assertEquals(AgendaStatus.STARTED, status);
    }

    @Test
    @DisplayName("Should return FINISHED when current time is after limit date")
    void shouldReturnFinished() {
        AgendaStatus status = calculator.calculate(
                LocalDateTime.of(2026, 8, 1, 10, 0),
                LocalDateTime.of(2026, 8, 1, 11, 0)
        );

        assertEquals(AgendaStatus.FINISHED, status);
    }

    @Test
    @DisplayName("Should return STARTED when current time equals limit date")
    void shouldReturnStartedWhenNowEqualsLimitDate() {
        AgendaStatus status = calculator.calculate(
                LocalDateTime.of(2026, 8, 1, 10, 0),
                LocalDateTime.of(2026, 8, 1, 12, 0)
        );

        assertEquals(AgendaStatus.STARTED, status);
    }
}
