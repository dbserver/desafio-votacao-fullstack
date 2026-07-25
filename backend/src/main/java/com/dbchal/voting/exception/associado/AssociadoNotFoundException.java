package com.dbchal.voting.exception.associado;

public class AssociadoNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public AssociadoNotFoundException(Long id) {
		super("Associado não encontrado com o ID " + id);
	}
}
