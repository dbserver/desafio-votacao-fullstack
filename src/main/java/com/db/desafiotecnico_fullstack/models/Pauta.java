package com.db.desafiotecnico_fullstack.models;


import com.db.desafiotecnico_fullstack.models.enuns.Status;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Table(name = "pauta")
@Entity(name = "pauta")
public class Pauta {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = true)
    private String descricao;

    @Column(nullable = false)
    private Integer tempoVotacao = 1; //default 1 min

    private boolean aberta;

    //getters e setters


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
