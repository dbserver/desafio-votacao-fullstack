package com.exemplo.projeto.adapter.input.dto;

import lombok.Data;

@Data
public class VotoRequest {
    private Long idPauta;
    private Long associadoId;
    private boolean voto;
}
