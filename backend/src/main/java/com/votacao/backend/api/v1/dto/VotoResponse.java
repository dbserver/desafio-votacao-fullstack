package com.votacao.backend.api.v1.dto;

import com.votacao.backend.domain.entity.Voto;
import com.votacao.backend.domain.enums.OpcaoVoto;

import java.time.LocalDateTime;
import java.util.UUID;

public record VotoResponse(UUID id, String associadoId, OpcaoVoto opcao, LocalDateTime votadoEm) {
    public static VotoResponse from(Voto v) {
        return new VotoResponse(v.getId(), v.getAssociadoId(), v.getOpcao(), v.getVotadoEm());
    }
}
