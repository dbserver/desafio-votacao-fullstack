package com.db.desafiotecnico_fullstack.models;


import com.db.desafiotecnico_fullstack.models.enuns.EscolhaVoto;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Data
@Table(name = "voto")
@Entity
public class Voto {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "pauta_id", nullable = false)
    private Pauta pauta;

    private UUID associadoId;

    @Enumerated(EnumType.STRING)
    private EscolhaVoto voto; //Sim ou nao

    //getters e setters

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Pauta getPauta() {
        return pauta;
    }

    public void setPauta(Pauta pauta) {
        this.pauta = pauta;
    }

    public UUID getAssociadoId() {
        return associadoId;
    }

    public void setAssociadoId(UUID associadoId) {
        this.associadoId = associadoId;
    }

    public EscolhaVoto getVoto() {
        return voto;
    }

    public void setVoto(EscolhaVoto voto) {
        this.voto = voto;
    }
}

