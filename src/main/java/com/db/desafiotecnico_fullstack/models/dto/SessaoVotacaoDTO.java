package com.db.desafiotecnico_fullstack.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Getter
@Setter
@AllArgsConstructor
public class SessaoVotacaoDTO {
    private UUID id;
    private UUID pautaId;
    private LocalDateTime inicio;
    private LocalDateTime fim;
    private boolean aberta;

}
