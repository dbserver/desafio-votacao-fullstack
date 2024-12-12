package com.db.desafiotecnico_fullstack.models.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@NoArgsConstructor
public class VotoDTO {
    private UUID pautaId;
    private UUID associadoId;
    private String voto;

    //getters e setters


    public UUID getPautaId() {
        return pautaId;
    }

    public void setPautaId(UUID pautaId) {
        this.pautaId = pautaId;
    }

    public UUID getAssociadoId() {
        return associadoId;
    }

    public void setAssociadoId(UUID associadoId) {
        this.associadoId = associadoId;
    }

    public String getVoto() {
        return voto;
    }

    public void setVoto(String voto) {
        this.voto = voto;
    }
}