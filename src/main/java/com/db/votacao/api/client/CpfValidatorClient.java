package com.db.votacao.api.client;

import com.db.votacao.api.dto.CpfValidationResponse;
import com.db.votacao.api.exception.ResourceNotFoundException;
import com.db.votacao.api.model.enums.VoteAbilityStatusEnum;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.regex.Pattern;

@Component
public class CpfValidatorClient {
    Random random = new Random();
    boolean canVote = random.nextBoolean();
    private static final Pattern CPF_PATTERN = Pattern.compile("\\d{11}");

    public CpfValidationResponse validateCpf(String cpf) {
        String cleanCpf = cpf.replaceAll("[^0-9]", "");

        if (!CPF_PATTERN.matcher(cleanCpf).matches()) {
            throw new ResourceNotFoundException("CPF inválido: " + cpf);
        }

        if (!isValidCpf(cleanCpf)) {
            throw new ResourceNotFoundException("CPF inválido: " + cpf);
        }

        return new CpfValidationResponse(
            canVote ? VoteAbilityStatusEnum.ABLE_TO_VOTE : VoteAbilityStatusEnum.UNABLE_TO_VOTE
        );
    }

    private boolean isValidCpf(String cpf) {
        if (cpf.length() != 11) return false;

        if (cpf.matches("(\\d)\\1{10}")) return false;

        int sum = 0;
        for (int i = 0; i < 9; i++) {
            sum += (cpf.charAt(i) - '0') * (10 - i);
        }
        int digit1 = 11 - (sum % 11);
        if (digit1 > 9) digit1 = 0;

        sum = 0;
        for (int i = 0; i < 10; i++) {
            sum += (cpf.charAt(i) - '0') * (11 - i);
        }
        int digit2 = 11 - (sum % 11);
        if (digit2 > 9) digit2 = 0;

        return (cpf.charAt(9) - '0' == digit1) && (cpf.charAt(10) - '0' == digit2);
    }
}
