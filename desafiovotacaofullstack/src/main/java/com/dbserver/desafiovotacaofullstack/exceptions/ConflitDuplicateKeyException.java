package com.dbserver.desafiovotacaofullstack.exceptions;

public class ConflitDuplicateKeyException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public ConflitDuplicateKeyException(String message) {
        super(message);
    }
}
