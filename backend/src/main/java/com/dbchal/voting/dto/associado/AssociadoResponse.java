package com.dbchal.voting.dto.associado;

import java.time.Instant;

import com.dbchal.voting.domain.entity.Associado;

public record AssociadoResponse(

		long id,
		String cpf,
		Instant dthCriacao
		
) {

	public static AssociadoResponse from(Associado associado) {
		return new AssociadoResponse(
				associado.getId(),
				associado.getCpf(),
				associado.getDthCriacao()
				);
	}
	
}
