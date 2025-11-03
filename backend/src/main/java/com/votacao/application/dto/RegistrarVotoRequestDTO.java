package com.votacao.application.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistrarVotoRequestDTO {

    @NotNull(message = "O ID da sessão é obrigatório")
    private Long idSessao;

    @NotNull(message = "A escolha do voto é obrigatória")
    private String escolha;
}
