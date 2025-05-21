package com.exemplo.projeto.adapter.input.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SessaoResponseDTO {
    private LocalDateTime inicio;
    private LocalDateTime fim;
}
