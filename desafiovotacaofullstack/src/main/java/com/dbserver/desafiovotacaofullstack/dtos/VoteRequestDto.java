package com.dbserver.desafiovotacaofullstack.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record VoteRequestDto(
		@NotNull(message = "Você deve informar a sessão.")
		Integer idSession,
		@NotNull(message = "Você deve informar o CPF do associado.")
		String cpfAssociate, 
		@NotBlank(message = "Você deve informar sua opção de voto.")
		String response) {

}
