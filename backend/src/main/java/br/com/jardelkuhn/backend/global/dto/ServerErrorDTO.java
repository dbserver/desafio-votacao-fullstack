package br.com.jardelkuhn.backend.global.dto;

import java.time.LocalDateTime;

public record ServerErrorDTO(String code, LocalDateTime dateTime, String message) {
}
