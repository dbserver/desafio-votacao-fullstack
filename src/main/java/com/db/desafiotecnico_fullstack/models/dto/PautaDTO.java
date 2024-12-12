package com.db.desafiotecnico_fullstack.models.dto;

import com.db.desafiotecnico_fullstack.models.Pauta;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import java.util.UUID;

public class PautaDTO {

    private UUID id;

    @NotBlank(message = "O título é obrigatório")
    @Size(min = 5, max = 100, message = "O título deve ter entre 5 e 100 caracteres")
    private String titulo;

    @Size(max = 500, message = "A descrição não pode ter mais que 500 caracteres")
    private String descricao;

    @Min(value = 1, message = "O tempo mínimo de votação é 1 minuto")
    @Max(value = 60, message = "O tempo máximo de votação é 60 minutos")
    private Integer tempoVotacao;

    private boolean aberta;

    // Construtores
    public PautaDTO() {}

    public PautaDTO(UUID id, String titulo, String descricao, Integer tempoVotacao, boolean aberta) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.tempoVotacao = tempoVotacao;
        this.aberta = aberta;
    }

    public PautaDTO(Pauta pauta) {
        this.id = pauta.getId();
        this.titulo = pauta.getTitulo();
        this.descricao = pauta.getDescricao();
        this.tempoVotacao = pauta.getTempoVotacao();
        this.aberta = pauta.isAberta();
    }

    // Getters e Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getTempoVotacao() {
        return tempoVotacao;
    }

    public void setTempoVotacao(Integer tempoVotacao) {
        this.tempoVotacao = tempoVotacao;
    }

    public boolean isAberta() {
        return aberta;
    }

    public void setAberta(boolean aberta) {
        this.aberta = aberta;
    }
}




