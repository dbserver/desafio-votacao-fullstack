package dev.rodrigo.desafiovotacao.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Dados necessários para criar uma Pauta")
public class PautaResponseDto {
  @Schema(description = "ID da Pauta", example = "10")
  private Long id;

  @Schema(description = "Título da pauta", example = "Aumento do Vale Refeição")
  private String titulo;

  @Schema(
      description = "Descrição da Pauta",
      example = "Votação para definir se devemos aumentar o valor do VR")
  private String descricao;

  @Schema(description = "Sessões da pauta")
  private List<SessaoResponseDto> sessoes;
}
