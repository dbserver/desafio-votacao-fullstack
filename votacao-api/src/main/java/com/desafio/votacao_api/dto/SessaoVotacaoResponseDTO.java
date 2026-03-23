package com.desafio.votacao_api.dto;

import com.desafio.votacao_api.model.SessaoVotacao;

import java.time.Instant;
import java.util.UUID;

/**
 * Data Transfer Object for returning SessaoVotacao (voting session) data to clients.
 *
 * This DTO exposes information about voting sessions including the session end time,
 * but excludes internal audit fields and the associated Pauta details.
 */
public record SessaoVotacaoResponseDTO(
        /**
         * Unique identifier of the voting session.
         */
        UUID id,

        /**
         * Unique identifier of the associated agenda.
         */
        UUID pautaId,

        /**
         * Date and time when the voting session was opened.
         */
        Instant createdAt,

        /**
         * Date and time when the voting session will close.
         */
        Instant finalSessionDateTime,

        /**
         * Indicates whether the session is currently active.
         */
        boolean isActive
) {
    /**
     * Creates a SessaoVotacaoResponseDTO from a SessaoVotacao entity.
     *
     * @param sessao the source SessaoVotacao entity
     * @return a new SessaoVotacaoResponseDTO instance
     */
    public static SessaoVotacaoResponseDTO fromEntity(SessaoVotacao sessao) {
        return new SessaoVotacaoResponseDTO(
                sessao.getId(),
                sessao.getPauta().getId(),
                sessao.getCreatedAt(),
                sessao.getFinalSessionDateTime(),
                sessao.isActive()
        );
    }
}

