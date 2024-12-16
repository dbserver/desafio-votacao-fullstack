package com.votacao.desafiovotacao.domain.services;

import com.votacao.desafiovotacao.domain.exceptions.CPFInvalidException;
import org.springframework.stereotype.Service;

import java.util.Random;


@Service
public class CPFValidationService {

    private final Random random = new Random();

    public boolean isValid(String cpf) {

        if (cpf == null || cpf.isEmpty()) {
            return false;
        }

        cpf = cpf.replaceAll("\\D", "");

        if (cpf.length() != 11) {
            return false;
        }

        if (cpf.matches("(\\d)\\1{10}")) {
            return false;
        }

        try {
            int[] wheigth = {1, 2, 3, 4, 5, 6, 7, 8, 9};
            int firstDigit = calculateCheckDigit(cpf, wheigth);

            wheigth = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
            int secondDigit = calculateCheckDigit(cpf, wheigth);

            return firstDigit == Character.getNumericValue(cpf.charAt(9)) &&
                    secondDigit == Character.getNumericValue(cpf.charAt(10));
        } catch (Exception e) {
            return false;
        }
    }

    private static int calculateCheckDigit(String cpf, int[] wheigth) {
        int sum = 0;
        for (int i = 0; i < wheigth.length; i++) {
            sum += Character.getNumericValue(cpf.charAt(i)) * wheigth[i];
        }
        int remainder = sum % 11;
        return remainder == 10 ? 0 : remainder;
    }

    public String isAbleToVote(String cpf) {
        return random.nextBoolean() ? "ABLE_TO_VOTE" : "UNABLE_TO_VOTE";
    }
}


