package com.dbchal.voting.dto.pauta;

import java.time.Instant;

import com.dbchal.voting.domain.entity.Pauta;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "PautaResponse", description = "Dados retornados de uma pauta")
public record PautaResponse(

		@Schema(description = "Identificador único da pauta", example = "1")
		Long id,

		@Schema(description = "Título da pauta", example = "Aprovação do orçamento anual")
		String titulo,

		@Schema(description = "Descrição detalhada da pauta", example = "Votação para aprovação do orçamento da cooperativa para o próximo ano")
		String descricao,

		@Schema(description = "Data e hora de criação da pauta", example = "2026-07-25T18:30:00Z")
		Instant dthCriacao

) {

	public static PautaResponse from(Pauta pauta) {
		return new PautaResponse(
				pauta.getId(),
				pauta.getTitulo(),
				pauta.getDescricao(),
				pauta.getDthCriacao()
		);
	}
}