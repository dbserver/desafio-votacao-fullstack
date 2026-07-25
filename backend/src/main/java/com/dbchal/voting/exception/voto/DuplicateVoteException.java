package com.dbchal.voting.exception.voto;

public class DuplicateVoteException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public DuplicateVoteException() {
		super("Você já votou nessa pauta");
	}
}
