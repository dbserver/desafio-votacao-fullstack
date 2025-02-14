package com.db.votacao.api.facade;

import com.db.votacao.api.client.CpfValidatorClient;
import com.db.votacao.api.dto.CpfValidationResponse;
import com.db.votacao.api.exception.BusinessException;
import com.db.votacao.api.model.enums.VoteAbilityStatusEnum;
import org.springframework.stereotype.Component;

@Component
public class CpfValidatorFacade {
    private final CpfValidatorClient cpfValidatorClient;

    public CpfValidatorFacade(CpfValidatorClient cpfValidatorClient) {
        this.cpfValidatorClient = cpfValidatorClient;
    }

    public void validateCpfForVoting(String cpf) {
        CpfValidationResponse response = cpfValidatorClient.validateCpf(cpf);

        if (response.getStatus() == VoteAbilityStatusEnum.UNABLE_TO_VOTE) {
            throw new BusinessException("CPF não está habilitado para votar");
        }
    }
}
