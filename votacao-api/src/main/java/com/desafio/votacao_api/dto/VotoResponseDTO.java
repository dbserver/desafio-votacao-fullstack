package com.desafio.votacao_api.dto;

import com.desafio.votacao_api.model.Voto;
import com.desafio.votacao_api.model.enums.VotoValue;
import java.util.UUID;

/**
 * Data Transfer Object for returning Voto (vote) data to clients.
 *
 * This DTO excludes sensitive information such as CPF and internal audit fields,
 * exposing only the vote itself and session reference information.
 */
public record VotoResponseDTO(
        /**
         * Unique identifier of the vote.
         */
        UUID id,

        /**
         * Unique identifier of the voting session.
         */
        UUID sessaoVotacaoId,

        /**
         * The vote value (YES or NO).
         */
        VotoValue vote,

        /**
         * Indicates whether the vote is active.
         */
        boolean isActive
) {
    /**
     * Creates a VotoResponseDTO from a Voto entity.
     *
     * @param voto the source Voto entity
     * @return a new VotoResponseDTO instance
     */
    public static VotoResponseDTO fromEntity(Voto voto) {
        return new VotoResponseDTO(
                voto.getId(),
                voto.getSessaoVotacao().getId(),
                voto.getVote(),
                voto.isActive()
        );
    }
}

