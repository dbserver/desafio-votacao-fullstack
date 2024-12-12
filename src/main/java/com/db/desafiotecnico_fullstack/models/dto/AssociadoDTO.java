package com.db.desafiotecnico_fullstack.models.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class AssociadoDTO {
    private UUID id;
    private String nome;
    private String cpf;

    //constructor

    public AssociadoDTO(UUID id, String nome, String cpf) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
    }
}
