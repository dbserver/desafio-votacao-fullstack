package com.service.polls.domain.exceptions;

import lombok.*;

import java.time.Instant;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ErrorDTO {
    private String code;
    private String message;
    private Instant timestamp;
}
