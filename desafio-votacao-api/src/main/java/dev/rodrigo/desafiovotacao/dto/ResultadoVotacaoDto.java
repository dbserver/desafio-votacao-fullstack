package dev.rodrigo.desafiovotacao.dto;

import dev.rodrigo.desafiovotacao.enums.ResultadoVotacao;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class ResultadoVotacaoDto {
  private Long sessaoId;
  private long votosSim;
  private long votosNao;
  private long total;
  private ResultadoVotacao resultado;
}
