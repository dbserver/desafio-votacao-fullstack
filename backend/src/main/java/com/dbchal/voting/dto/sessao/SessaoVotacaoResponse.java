package com.dbchal.voting.dto.sessao;

import java.time.Instant;

import com.dbchal.voting.domain.entity.SessaoVotacao;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "SessaoVotacaoResponse", description = "Dados retornados de uma sessão de votação")
public record SessaoVotacaoResponse(

		@Schema(description = "Identificador único da sessão de votação", example = "1")
		Long id,

		@Schema(description = "Identificador da pauta vinculada à sessão", example = "10")
		Long pautaId,

		@Schema(description = "Data e hora de abertura da sessão", example = "2026-07-25T18:30:00Z")
		Instant dthAbertura,

		@Schema(description = "Data e hora de encerramento da sessão", example = "2026-07-25T18:35:00Z")
		Instant dthFim,

		@Schema(description = "Indica se a sessão ainda está aberta para votação", example = "true")
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