package com.dbchal.voting.dto.voto;

import com.dbchal.voting.domain.enums.OpcaoVoto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateVotoRequest(

		@NotNull(message = "É necessário um associado para votar")
		@Positive(message = "O ID do associado deve ser maior que zero")
		Long associadoId,

		@NotNull(message = "É obrigatório selecionar uma opção de voto")
		OpcaoVoto opcao
		
) {

}
