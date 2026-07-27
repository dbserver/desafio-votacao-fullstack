package com.dbchal.voting.dto.pauta;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(name = "CreatePautaRequest", description = "Dados necessários para cadastrar uma pauta")
public record CreatePautaRequest(

		@Schema(description = "Título da pauta", example = "Aprovação do orçamento anual", maxLength = 255, requiredMode = Schema.RequiredMode.REQUIRED)
		@NotBlank(message = "Title is required")
		@Size(max = 255)
		String titulo,

		@Schema(description = "Descrição detalhada da pauta", example = "Votação para aprovação do orçamento da cooperativa para o próximo ano", maxLength = 1000)
		@Size(max = 1000)
		String descricao

) {
}