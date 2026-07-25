package com.dbchal.voting.dto.voto;

import com.dbchal.voting.domain.enums.ResultadoVotacaoStatus;

public record ResultadoVotacaoResponse(
		
		Long pautaId,
		Long sessaoId,
		long votosSim,
		long votosNao,
		long totalVotos,
		boolean sessaoAberta,
		ResultadoVotacaoStatus resultado
			
) {
}
