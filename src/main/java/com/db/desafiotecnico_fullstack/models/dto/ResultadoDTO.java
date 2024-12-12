package com.db.desafiotecnico_fullstack.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor  // Construtor sem argumentos
public class ResultadoDTO {
    private UUID pautaId;
    private String titulo;
    private long votosSim;
    private long votosNao;
    private long totalVotos;

    // Construtor explícito
    public ResultadoDTO(UUID pautaId, String titulo, long votosSim, long votosNao, long totalVotos) {
        this.pautaId = pautaId;
        this.titulo = titulo;
        this.votosSim = votosSim;
        this.votosNao = votosNao;
        this.totalVotos = totalVotos;
    }

    //getters e setters

    public UUID getPautaId() {
        return pautaId;
    }

    public void setPautaId(UUID pautaId) {
        this.pautaId = pautaId;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public long getVotosSim() {
        return votosSim;
    }

    public void setVotosSim(long votosSim) {
        this.votosSim = votosSim;
    }

    public long getVotosNao() {
        return votosNao;
    }

    public void setVotosNao(long votosNao) {
        this.votosNao = votosNao;
    }

    public long getTotalVotos() {
        return totalVotos;
    }

    public void setTotalVotos(long totalVotos) {
        this.totalVotos = totalVotos;
    }
}