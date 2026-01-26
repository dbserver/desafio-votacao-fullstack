package com.service.polls.domain.model;

import com.service.polls.application.dto.PollDTO;
import lombok.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Poll {
    private Long id;
    private String title;
    private Instant startsAt;
    private Instant endsAt;

    public static Poll of(PollDTO pollDTO) {
        Instant now = Instant.now();
        Instant endsAt = now.plus(pollDTO.getDurationMinutes(), ChronoUnit.MINUTES);
        return Poll.builder()
                .title(pollDTO.getTitle())
                .startsAt(now)
                .endsAt(endsAt)
                .build();
    }

    public boolean isOpen(Instant now) {
        return !now.isBefore(startsAt) && now.isBefore(endsAt);
    }
}
