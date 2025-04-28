package com.desafiofullstask.votacao.util;

import org.springframework.stereotype.Service;

@Service
public class ValidationUtil {

    /**
     * Essa função realiza a validação do CPF.
     */

    public static boolean validateCPF(String cpf) {

            if (cpf.isEmpty()) return false;
            String number = cpf;

            while (number.length() < 11) {
                number = "0" + number;
            }

            String filteredNumber = number.replaceAll("[^\\d]", "");
            if (filteredNumber.length() != 11) {
                return false;
            }

            int[] numbers = new int[11];
            for (int i = 0; i < 11; i++) {
                numbers[i] = Character.getNumericValue(filteredNumber.charAt(i));
            }

            boolean allEqual = true;
            for (int i = 1; i < numbers.length; i++) {
                if (numbers[i] != numbers[0]) {
                    allEqual = false;
                    break;
                }
            }
            if (allEqual) return false;

            int dv1 = 0;
            for (int i = 0; i <= 8; i++) {
                dv1 += (i + 1) * numbers[i];
            }
            dv1 = dv1 % 11;
            if (dv1 >= 10) dv1 = 0;

            int dv2 = 0;
            for (int i = 0; i <= 8; i++) {
                dv2 += i * numbers[i];
            }
            dv2 = (dv2 + (dv1 * 9)) % 11;
            if (dv2 >= 10) dv2 = 0;

        return numbers[9] == dv1 && numbers[10] == dv2;
        }
    }

