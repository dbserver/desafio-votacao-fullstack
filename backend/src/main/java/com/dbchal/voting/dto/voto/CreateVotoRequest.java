package com.dbchal.voting.dto.voto;

import com.dbchal.voting.domain.enums.OpcaoVoto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(name = "CreateVotoRequest", description = "Dados necessários para registrar um voto" )
public record CreateVotoRequest(

		@Schema(description = "Identificador do associado que realizará o voto", example = "20", minimum = "1", requiredMode = Schema.RequiredMode.REQUIRED)
		@NotNull(message = "É necessário um associado para votar")
		@Positive(message = "O ID do associado deve ser maior que zero")
		Long associadoId,

		@Schema(description = "Opção escolhida pelo associado", example = "SIM", allowableValues = { "SIM", "NAO" }, requiredMode = Schema.RequiredMode.REQUIRED)
		@NotNull(message = "É obrigatório selecionar uma opção de voto")
		OpcaoVoto opcao

) {
}