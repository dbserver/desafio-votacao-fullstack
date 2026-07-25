package com.dbchal.voting.exception.voto;

public class AlreadyVotedException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public AlreadyVotedException() {
		super("Você já votou nessa pauta");
	}
}
