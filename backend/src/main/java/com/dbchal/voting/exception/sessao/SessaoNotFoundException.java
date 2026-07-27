package com.dbchal.voting.exception.sessao;

public class SessaoNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public SessaoNotFoundException(Long pautaId) {
		super("Não existe sessão aberta para a pauta " + pautaId);
	}
}
