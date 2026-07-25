package com.dbchal.voting.dto;

import java.time.Instant;

import com.dbchal.voting.domain.Pauta;

public record PautaResponse (
		Long id,
		String titulo,
		String descricao,
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
