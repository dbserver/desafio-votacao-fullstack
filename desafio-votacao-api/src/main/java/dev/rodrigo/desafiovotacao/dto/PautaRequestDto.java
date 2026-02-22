package dev.rodrigo.desafiovotacao.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Dados necessários para criar uma nova pauta")
public class PautaRequestDto {
  @Schema(description = "Título da Pauta", example = "Aumento VR")
  @NotBlank
  private String titulo;

  @Schema(description = "Descrição da Pauta", example = "Votação para definir aumento VR")
  @NotBlank
  private String descricao;
}
