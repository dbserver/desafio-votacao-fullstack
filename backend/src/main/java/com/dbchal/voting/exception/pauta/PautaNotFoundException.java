package com.dbchal.voting.exception.pauta;

public class PautaNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public PautaNotFoundException(Long id) {
		super("A pauta " + id + " não foi encontrada");
	}
}
