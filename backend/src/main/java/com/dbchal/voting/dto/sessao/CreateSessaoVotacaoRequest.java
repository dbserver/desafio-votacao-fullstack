package com.dbchal.voting.dto.sessao;

import jakarta.validation.constraints.Positive;

public record CreateSessaoVotacaoRequest(

		@Positive(message = "A duração da sessão deve ser maior que 0")
		Long duracao
) {

}
