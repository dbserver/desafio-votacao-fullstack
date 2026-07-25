package com.dbchal.voting.dto;

import jakarta.validation.constraints.Positive;

public record OpenSessaoVotacaoRequest(

		@Positive(message = "A duração da sessão deve ser maior que 0")
		Long duracao
) {

}
