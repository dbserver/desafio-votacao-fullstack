package dev.rodrigo.desafiovotacao.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class SessaoResponseDto {
  private Long id;
  private Long pautaId;
  private LocalDateTime dataAbertura;
  private LocalDateTime dataFechamento;
  private boolean aberta;
}
