package com.dbchal.voting.dto.pauta;

import java.time.Instant;

import com.dbchal.voting.domain.entity.Pauta;

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
