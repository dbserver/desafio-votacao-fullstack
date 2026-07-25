package com.dbchal.voting.dto.associado;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CreateAssociadoRequest(

		@NotBlank(message = "O CPF é obrigatório")
		@Pattern(regexp = "\\d{11}", message = "O CPF deve possuir exatamente 11 números")
		String cpf
		
) {

}
