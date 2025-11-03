package com.votacao.application.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PautaRequestDTO {
    @NotBlank(message = "O título é obrigatório")
    private String titulo;
    private String descricao;
}
