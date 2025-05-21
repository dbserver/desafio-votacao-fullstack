package com.exemplo.projeto.adapter.input.dto;

import lombok.Data;

@Data
public class ResultadoVotacaoDTO {
    private Long idPauta;
    private String titulo;
    private String descricao;
    private long totalVotos;
    private long votosSim;
    private long votosNao;
    private String resultado;

    public ResultadoVotacaoDTO(Long idPauta, String titulo, String descricao, long totalVotos, long votosSim, long votosNao, String resultado) {
        this.idPauta = idPauta;
        this.titulo = titulo;
        this.descricao = descricao;
        this.totalVotos = totalVotos;
        this.votosSim = votosSim;
        this.votosNao = votosNao;
        this.resultado = resultado;
    }
}
