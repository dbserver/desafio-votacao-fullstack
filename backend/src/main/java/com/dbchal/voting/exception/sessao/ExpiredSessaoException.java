package com.dbchal.voting.exception.sessao;

public class ExpiredSessaoException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ExpiredSessaoException(Long pautaId) {
		super("Sessão de votação já foi finalizada para pauta " + pautaId);
	}

}
