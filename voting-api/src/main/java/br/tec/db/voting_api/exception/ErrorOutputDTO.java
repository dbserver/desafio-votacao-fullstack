package br.tec.db.voting_api.exception;

import java.time.LocalDateTime;

public record ErrorOutputDTO(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        String path
) { }
