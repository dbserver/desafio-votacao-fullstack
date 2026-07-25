package com.dbchal.voting.exception;

public class AlreadyExistingSessionException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public AlreadyExistingSessionException(Long pautaId) {
		super("Já existe uma sessão aberta para a pauta " + pautaId);
	}

}
