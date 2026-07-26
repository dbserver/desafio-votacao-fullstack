package com.dbchal.voting.dto.associado;

import java.time.Instant;

import com.dbchal.voting.domain.entity.Associado;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "AssociadoResponse", description = "Dados retornados de um associado")
public record AssociadoResponse(

		@Schema(description = "Identificador único do associado", example = "1")
		long id,

		@Schema(description = "CPF do associado, contendo exatamente 11 números", example = "12345678901")
		String cpf,

		@Schema(description = "Data e hora de criação do associado", example = "2026-07-25T18:30:00Z")
		Instant dthCriacao

) {

	public static AssociadoResponse from(Associado associado) {
		return new AssociadoResponse(
				associado.getId(),
				associado.getCpf(),
				associado.getDthCriacao()
		);
	}

}