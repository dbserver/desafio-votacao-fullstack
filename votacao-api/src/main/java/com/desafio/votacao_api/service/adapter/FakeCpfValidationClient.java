package com.desafio.votacao_api.service.adapter;

import com.desafio.votacao_api.exception.CpfNotFoundException;
import com.desafio.votacao_api.exception.UnableToVoteException;
import com.desafio.votacao_api.service.facade.CpfValidationFacade;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

@Component
public class FakeCpfValidationClient implements CpfValidationFacade {

    @Override
    public void validateCpfNumber(String cpf) {
        if (cpf == null || cpf.isBlank()) {
            throw new CpfNotFoundException("CPF not found.");
        }

        boolean cpfExists = ThreadLocalRandom.current().nextBoolean();
        if (!cpfExists) {
            throw new CpfNotFoundException("CPF not found.");
        }

        VoteStatus status = ThreadLocalRandom.current().nextBoolean() ? VoteStatus.ABLE_TO_VOTE : VoteStatus.UNABLE_TO_VOTE;
        if (status == VoteStatus.UNABLE_TO_VOTE) {
            throw new UnableToVoteException("CPF is unable to vote.");
        }
    }

    private enum VoteStatus {
        ABLE_TO_VOTE,
        UNABLE_TO_VOTE
    }
}