package com.exemplo.projeto.domain.model;

import lombok.Data;

@Data
public class Pauta {
    private Long id;
    private String titulo;
    private String descricao;
    private Sessao sessao;
}
