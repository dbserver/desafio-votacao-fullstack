package com.dbchal.voting.dto.voto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ResultadoVotosProjection", description = "Projeção da quantidade de votos agrupados por opção")
public interface ResultadoVotosProjection {

	@Schema(description = "Quantidade de votos na opção SIM", example = "15")
	Long getVotosSim();

	@Schema(description = "Quantidade de votos na opção NÃO", example = "8")
	Long getVotosNao();

}