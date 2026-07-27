package com.dbchal.voting.dto.associado;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Schema(name = "CreateAssociadoRequest", description = "Dados necessários para cadastrar um associado")
public record CreateAssociadoRequest(

		@Schema(description = "CPF do associado, contendo exatamente 11 números", example = "12345678901", requiredMode = Schema.RequiredMode.REQUIRED)
		@NotBlank(message = "O CPF é obrigatório")
		@Pattern(regexp = "\\d{11}", message = "O CPF deve possuir exatamente 11 números")
		String cpf

) {

}