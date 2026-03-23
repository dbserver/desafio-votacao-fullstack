package com.desafio.votacao_api.dto;

import java.time.Instant;

/**
 * Data Transfer Object for standardized error responses.
 *
 * This DTO is used to return consistent error information to API clients.
 *
 * @param timestamp the instant when the error occurred
 * @param status HTTP status code
 * @param error error type or title
 * @param message error description message
 * @param path the API path where the error occurred
 */
public record ErrorResponseDTO(
        Instant timestamp,
        int status,
        String error,
        String message,
        String path
) {
}

