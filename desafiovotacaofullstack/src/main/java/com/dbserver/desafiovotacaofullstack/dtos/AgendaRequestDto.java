package com.dbserver.desafiovotacaofullstack.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AgendaRequestDto(
		@NotBlank(message = "A descrição não pode estar em branco.")
		@Size(min = 10, max = 1000, message = "A descrição deve ter no minimo 10 e no máximo 1.000 caracteres.")
		String description
		) {

}
