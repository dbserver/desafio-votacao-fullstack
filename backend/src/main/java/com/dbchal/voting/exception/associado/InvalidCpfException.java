package com.dbchal.voting.exception.associado;

public class InvalidCpfException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public InvalidCpfException() {
		super("CPF Inválido.");
	}
}