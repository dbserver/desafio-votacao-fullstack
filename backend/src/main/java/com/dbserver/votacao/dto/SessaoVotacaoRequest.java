package com.dbserver.votacao.dto;

import jakarta.validation.constraints.NotNull;

public record SessaoVotacaoRequest(
        @NotNull(message = "ID da pauta é obrigatório")
        Long pautaId,
        Integer duracaoMinutos
) {}
