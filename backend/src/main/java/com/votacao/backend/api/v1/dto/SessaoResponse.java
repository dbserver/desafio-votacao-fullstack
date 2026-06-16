package com.votacao.backend.api.v1.dto;

import com.votacao.backend.domain.entity.Sessao;

import java.time.LocalDateTime;
import java.util.UUID;

public record SessaoResponse(UUID id, UUID pautaId, LocalDateTime abertaEm, LocalDateTime expiraEm, boolean encerrada) {
    public static SessaoResponse from(Sessao s) {
        return new SessaoResponse(
                s.getId(),
                s.getPauta().getId(),
                s.getAbertaEm(),
                s.getExpiraEm(),
                s.getEncerradaEm() != null || !s.isAtiva()
        );
    }
}
