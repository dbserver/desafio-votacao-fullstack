package com.dbchal.voting.exception;

public class CreateEntityException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public CreateEntityException(String entity) {
		super("Ocorreu um erro na criação de " + entity);
	}

}
