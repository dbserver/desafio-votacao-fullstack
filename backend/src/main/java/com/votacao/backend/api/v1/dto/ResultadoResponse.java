package com.votacao.backend.api.v1.dto;

import java.util.UUID;

public record ResultadoResponse(UUID pautaId, UUID sessaoId, long sim, long nao, long total, boolean encerrada) {
    public static ResultadoResponse from(ResultadoResponse dto) {
        return new ResultadoResponse(dto.pautaId(), dto.sessaoId(), dto.sim(), dto.nao(), dto.total(), dto.encerrada());
    }
}
