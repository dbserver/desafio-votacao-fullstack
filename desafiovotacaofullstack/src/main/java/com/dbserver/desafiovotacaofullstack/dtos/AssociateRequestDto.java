package com.dbserver.desafiovotacaofullstack.dtos;

import jakarta.validation.constraints.NotBlank;

public record AssociateRequestDto(
		@NotBlank(message = "O nome não pode estar em branco.")
		String name,
		@NotBlank(message = "O CPF não pode estar em branco.")
		String cpf
		) {

}
