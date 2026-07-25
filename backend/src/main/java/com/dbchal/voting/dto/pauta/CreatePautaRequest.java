package com.dbchal.voting.dto.pauta;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreatePautaRequest(

		@NotBlank(message = "Title is required")
		@Size(max = 255) 
		String titulo,

		@Size(max = 1000) 
		String descricao
) {
}
