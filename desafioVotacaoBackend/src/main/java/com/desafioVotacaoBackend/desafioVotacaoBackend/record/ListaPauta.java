package com.desafioVotacaoBackend.desafioVotacaoBackend.record;

import com.desafioVotacaoBackend.desafioVotacaoBackend.model.Pauta;
import java.time.LocalDateTime;

public record ListaPauta(
                Long id,
                String titulo,
                String descricao,
                LocalDateTime dataAbertura,
                LocalDateTime dataFechamento,
                Boolean sessaoAtiva) {
        public ListaPauta(Pauta pauta) {
                this(
                                pauta.getId(),
                                pauta.getTitulo(),
                                pauta.getDescricao(),
                                pauta.getDataAbertura(),
                                pauta.getDataFechamento(),
                                pauta.getSessaoAtiva());
        }
}
