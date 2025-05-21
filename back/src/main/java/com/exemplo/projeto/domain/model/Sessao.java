package com.exemplo.projeto.domain.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Sessao {
    private LocalDateTime inicio;
    private LocalDateTime fim;
    private Long idPauta;

    public Sessao() {
        this.inicio = LocalDateTime.now();
    }

    public Sessao(LocalDateTime inicio, LocalDateTime fim, Long idPauta) {
        this.inicio = inicio;
        this.fim = fim;
        this.idPauta = idPauta;
    }
}
