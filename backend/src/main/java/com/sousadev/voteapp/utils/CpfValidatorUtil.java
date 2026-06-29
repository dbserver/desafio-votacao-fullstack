package com.sousadev.voteapp.utils;

import com.sousadev.voteapp.enums.CpfVoteStatus;

public final class CpfValidatorUtil {

    private static final int CPF_LENGTH = 11;

    private CpfValidatorUtil() {
    }

    public static CpfVoteStatus validate(String cpf) {
        return isValid(cpf)
                ? CpfVoteStatus.ABLE_TO_VOTE
                : CpfVoteStatus.UNABLE_TO_VOTE;
    }

    public static boolean isValid(String cpf) {
        if (cpf == null) {
            return false;
        }

        String numbers = normalize(cpf);

        if (numbers.length() != CPF_LENGTH || hasAllEqualDigits(numbers)) {
            return false;
        }

        int firstDigit = calculateDigit(numbers, 9);
        int secondDigit = calculateDigit(numbers, 10);

        return Character.getNumericValue(numbers.charAt(9)) == firstDigit
                && Character.getNumericValue(numbers.charAt(10)) == secondDigit;
    }

    public static String normalize(String cpf) {
        return cpf == null ? "" : cpf.replaceAll("\\D", "");
    }

    private static boolean hasAllEqualDigits(String cpf) {
        return cpf.chars().distinct().count() == 1;
    }

    private static int calculateDigit(String cpf, int length) {
        int sum = 0;

        for (int index = 0; index < length; index++) {
            sum += Character.getNumericValue(cpf.charAt(index)) * (length + 1 - index);
        }

        int remainder = sum % 11;
        return remainder < 2 ? 0 : 11 - remainder;
    }
}
