package com.dbchal.voting.exception.associado;

public class CpfAlreadyRegisteredException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public CpfAlreadyRegisteredException(String cpf) {
		super("Já existe um associado cadastrado com o CPF informado");
	}
}
