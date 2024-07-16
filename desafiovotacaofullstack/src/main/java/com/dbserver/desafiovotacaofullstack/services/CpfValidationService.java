package com.dbserver.desafiovotacaofullstack.services;

import java.util.Random;

import org.springframework.stereotype.Service;

@Service
public class CpfValidationService {
	
	private final Random random = new Random();

    public String canVote() {
        return random.nextBoolean() ? "ABLE_TO_VOTE" : "UNABLE_TO_VOTE";
    }
}
