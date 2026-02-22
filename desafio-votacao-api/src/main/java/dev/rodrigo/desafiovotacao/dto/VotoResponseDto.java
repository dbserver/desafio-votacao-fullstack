package dev.rodrigo.desafiovotacao.dto;

import dev.rodrigo.desafiovotacao.enums.TipoVoto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Schema(description = "Dados retornados após o registro do voto")
public class VotoResponseDto {

  @Schema(description = "ID do voto", example = "1")
  private Long id;

  @Schema(description = "ID da sessão", example = "10")
  private Long sessaoId;

  @Schema(description = "CPF do associado")
  private String cpf;

  @Schema(description = "Tipo do voto")
  private TipoVoto voto;

  @Schema(description = "Data e hora do voto")
  private LocalDateTime dataHora;
}
