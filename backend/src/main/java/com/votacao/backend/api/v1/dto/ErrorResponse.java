package com.votacao.backend.api.v1.dto;

import java.time.LocalDateTime;

public record ErrorResponse(int status, String mensagem, LocalDateTime timestamp) {
}
