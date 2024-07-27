package com.dbserver.desafiovotacaofullstack.dtos;

import com.dbserver.desafiovotacaofullstack.constraints.CpfConstraint;

import jakarta.validation.constraints.NotBlank;

public record AssociateRequestDto(
		@NotBlank(message = "O nome não pode estar em branco.")
		String name,
		@NotBlank(message = "O CPF não pode estar em branco.")
		@CpfConstraint(message = "O CPF deve conter apenas caracteres válidos.")
		String cpf
		) {
	
	

}
