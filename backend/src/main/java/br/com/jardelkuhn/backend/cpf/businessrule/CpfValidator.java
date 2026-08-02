package br.com.jardelkuhn.backend.cpf.businessrule;

public class CpfValidator {
    public static boolean isCpf(String cpf) {
        if (cpf == null) {
            return false;
        }

        // remove separadores
        cpf = cpf.replaceAll("\\D", "");

        // deve ter exatamente 11 digitos
        if (cpf.length() != 11) {
            return false;
        }

        // rejeita cpf preenchido com números identicos
        // ex: 11111111111 22222222222 ...
        if (cpf.matches("(\\d)\\1{10}")) {
            return false;
        }

        // 11 digitos, 2 ultimos são verificadores (DV)
        // DV é calculado com base nos dígitos anteriores

        // os dois ultimos digitos são verificadores
        int number10 = Character.getNumericValue(cpf.charAt(9));
        int number11 = Character.getNumericValue(cpf.charAt(10));

        // verificar o digito verificador 1
        int dv1 = calculateDV1(cpf);

        if (dv1 != number10) {
            return false;
        }

        // verificar o digito verificador 2
        int dv2 = calculateDV2(cpf);

        if (dv2 != number11) {
            return false;
        }

        return true;
    }

    /**
     * calcular o 1º dígito verificador
     */
    private static int calculateDV1(String cpf) {
        // extrair os numeros
        int number1 = Character.getNumericValue(cpf.charAt(0));
        int number2 = Character.getNumericValue(cpf.charAt(1));
        int number3 = Character.getNumericValue(cpf.charAt(2));
        int number4 = Character.getNumericValue(cpf.charAt(3));
        int number5 = Character.getNumericValue(cpf.charAt(4));
        int number6 = Character.getNumericValue(cpf.charAt(5));
        int number7 = Character.getNumericValue(cpf.charAt(6));
        int number8 = Character.getNumericValue(cpf.charAt(7));
        int number9 = Character.getNumericValue(cpf.charAt(8));

        // multiplicacao descrescente
        int multiplication1 = number1 * 10;
        int multiplication2 = number2 * 9;
        int multiplication3 = number3 * 8;
        int multiplication4 = number4 * 7;
        int multiplication5 = number5 * 6;
        int multiplication6 = number6 * 5;
        int multiplication7 = number7 * 4;
        int multiplication8 = number8 * 3;
        int multiplication9 = number9 * 2;

        // soma
        int sum = multiplication1
                + multiplication2
                + multiplication3
                + multiplication4
                + multiplication5
                + multiplication6
                + multiplication7
                + multiplication8
                + multiplication9;

        // divisao por primo 11
        int primeModule = sum % 11;

        // subtracao 11 - resultado
        int dv1 = 11 - primeModule;

        // se for 10 ou maior, o DV1 será o 0
        if (dv1 >= 10) {
            dv1 = 0;
        }

        return dv1;
    }

    /**
     * calcular o 2º digito verificador
     */
    private static int calculateDV2(String cpf) {
        // extrair os numeros
        int number1 = Character.getNumericValue(cpf.charAt(0));
        int number2 = Character.getNumericValue(cpf.charAt(1));
        int number3 = Character.getNumericValue(cpf.charAt(2));
        int number4 = Character.getNumericValue(cpf.charAt(3));
        int number5 = Character.getNumericValue(cpf.charAt(4));
        int number6 = Character.getNumericValue(cpf.charAt(5));
        int number7 = Character.getNumericValue(cpf.charAt(6));
        int number8 = Character.getNumericValue(cpf.charAt(7));
        int number9 = Character.getNumericValue(cpf.charAt(8));
        int number10 = Character.getNumericValue(cpf.charAt(9));

        // multiplicacao descrescente
        int multiplication1 = number1 * 11;
        int multiplication2 = number2 * 10;
        int multiplication3 = number3 * 9;
        int multiplication4 = number4 * 8;
        int multiplication5 = number5 * 7;
        int multiplication6 = number6 * 6;
        int multiplication7 = number7 * 5;
        int multiplication8 = number8 * 4;
        int multiplication9 = number9 * 3;
        int multiplication10 = number10 * 2;

        // soma
        int sum = multiplication1
                + multiplication2
                + multiplication3
                + multiplication4
                + multiplication5
                + multiplication6
                + multiplication7
                + multiplication8
                + multiplication9
                + multiplication10;

        // divisao por primo 11
        int primeModule = sum % 11;

        // subtracao 11 - resultado
        int dv2 = 11 - primeModule;

        // se for 10 ou maior, o DV2 será o 0
        if (dv2 >= 10) {
            dv2 = 0;
        }

        return dv2;
    }
}
