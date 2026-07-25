package com.dbchal.voting.exception.sessao;

public class AlreadyExistingSessaoException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public AlreadyExistingSessaoException(Long pautaId) {
		super("Já existe uma sessão aberta para a pauta " + pautaId);
	}

}
