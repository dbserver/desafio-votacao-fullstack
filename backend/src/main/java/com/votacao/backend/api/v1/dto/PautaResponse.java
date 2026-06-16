package com.votacao.backend.api.v1.dto;

import com.votacao.backend.domain.entity.Pauta;

import java.time.LocalDateTime;
import java.util.UUID;

public record PautaResponse(UUID id, String titulo, String descricao, LocalDateTime criadoEm) {
    public static PautaResponse from(Pauta p) {
        return new PautaResponse(p.getId(), p.getTitulo(), p.getDescricao(), p.getCriadoEm());
    }
}
