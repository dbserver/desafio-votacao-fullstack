package com.dbchal.voting.dto.voto;

import com.dbchal.voting.domain.enums.ResultadoVotacaoStatus;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ResultadoVotacaoResponse", description = "Resultado atual de uma sessão de votação")
public record ResultadoVotacaoResponse(

		@Schema(description = "Identificador da pauta", example = "1")
		Long pautaId,

		@Schema(description = "Identificador da sessão de votação", example = "10")
		Long sessaoId,

		@Schema(description = "Quantidade de votos na opção SIM", example = "15")
		long votosSim,

		@Schema(description = "Quantidade de votos na opção NÃO", example = "8")
		long votosNao,

		@Schema(description = "Quantidade total de votos registrados", example = "23")
		long totalVotos,

		@Schema(description = "Indica se a sessão ainda está aberta para votação", example = "false")
		boolean sessaoAberta,

		@Schema(description = "Situação atual do resultado da votação", example = "APROVADA")
		ResultadoVotacaoStatus resultado

) {
}