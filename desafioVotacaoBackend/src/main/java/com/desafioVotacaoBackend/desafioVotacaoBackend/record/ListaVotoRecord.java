package com.desafioVotacaoBackend.desafioVotacaoBackend.record;

import java.time.LocalDateTime;

public record ListaVotoRecord(
        Long id,
        LocalDateTime dataVoto,
        String opcao,
        Long associadoId,
        Long pautaId) {
}