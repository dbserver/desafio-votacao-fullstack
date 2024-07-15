package com.dbserver.desafiovotacaofullstack.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AssociateRequestDto(
		@NotBlank(message = "O nome não pode estar em branco.")
		String name,
		@NotBlank(message = "O CPF não pode estar em branco.")
		@Size(min = 11, max = 11, message = "O CPF deve ter exatamente 11 dígitos.")
		String cpf
		) {

}
