package com.dbchal.voting.dto.sessao;

import java.time.Instant;

import com.dbchal.voting.domain.entity.SessaoVotacao;

public record SessaoVotacaoResponse(

		Long id,
		Long pautaId,
		Instant dthAbertura,
		Instant dthFim,
		boolean open
		
) {

	public static SessaoVotacaoResponse from(SessaoVotacao sessao) {
		return new SessaoVotacaoResponse(
					sessao.getId(),
					sessao.getPauta().getId(),
					sessao.getDthInicio(),
					sessao.getDthFim(),
					sessao.isOpen()
				);
	}
	
}
