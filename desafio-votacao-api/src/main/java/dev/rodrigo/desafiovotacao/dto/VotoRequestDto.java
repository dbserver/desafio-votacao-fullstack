package dev.rodrigo.desafiovotacao.dto;

import dev.rodrigo.desafiovotacao.enums.TipoVoto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NonNull;

@Data
@Schema(description = "Dados necessários para registrar um voto")
public class VotoRequestDto {

  @Schema(description = "CPF do associado (somente números)", example = "430.691.550-65")
  @NotBlank private String cpf;

  @Schema(description = "Tipo do voto: SIM ou NAO", example = "SIM")
  @NonNull private TipoVoto voto;

  public VotoRequestDto() {
    
  }
}
