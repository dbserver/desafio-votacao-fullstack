package com.dbchal.voting.dto.voto;

import java.time.Instant;

import com.dbchal.voting.domain.entity.Voto;
import com.dbchal.voting.domain.enums.OpcaoVoto;

public record VotoResponse(

		Long id,
		Long sessaoId,
		Long pautaId,
		Long associadoId,
		OpcaoVoto opcao,
		Instant dthVoto

) {

	public static VotoResponse from(Voto voto) {
		return new VotoResponse(
				voto.getId(),
				voto.getSessao().getId(),
				voto.getSessao().getPauta().getId(),
				voto.getAssociado().getId(),
				voto.getOpcao(),
				voto.getDthVoto()
		);
	}
}