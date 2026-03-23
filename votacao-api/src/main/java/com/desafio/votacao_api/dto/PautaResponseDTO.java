package com.desafio.votacao_api.dto;

import com.desafio.votacao_api.model.Pauta;

import java.util.UUID;

/**
 * Data Transfer Object for returning Pauta (agenda) data to clients.
 *
 * This DTO only exposes non-sensitive information about an agenda,
 * excluding internal timestamps and status flags.
 */
public record PautaResponseDTO(
        /**
         * Unique identifier of the agenda.
         */
        UUID id,

        /**
         * Name of the agenda.
         */
        String name,

        /**
         * Description of the agenda.
         */
        String description,

        /**
         * Indicates whether the agenda is active for voting.
         */
        boolean isActive
) {
    /**
     * Creates a PautaResponseDTO from a Pauta entity.
     *
     * @param pauta the source Pauta entity
     * @return a new PautaResponseDTO instance
     */
    public static PautaResponseDTO fromEntity(Pauta pauta) {
        return new PautaResponseDTO(
                pauta.getId(),
                pauta.getName(),
                pauta.getDescription(),
                pauta.isActive()
        );
    }
}

