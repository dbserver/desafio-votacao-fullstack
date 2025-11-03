package com.votacao.application.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SessaoVotacaoRequestDTO {
    @NotNull(message = "O ID da pauta é obrigatório")
    private Long idPauta;
    private LocalDateTime inicioSessao;
    private LocalDateTime fimSessao;
}
