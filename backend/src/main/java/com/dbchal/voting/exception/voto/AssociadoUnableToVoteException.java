package com.dbchal.voting.exception.voto;

public class AssociadoUnableToVoteException
		extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public AssociadoUnableToVoteException() {
		super("O associado não está habilitado para votar");
	}
}