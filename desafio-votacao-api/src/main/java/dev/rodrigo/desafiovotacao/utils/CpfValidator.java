package dev.rodrigo.desafiovotacao.utils;

public class CpfValidator {

  public static boolean isValid(String cpf) {
    if (cpf == null) return false;

    cpf = cpf.replaceAll("\\D", "");

    if (cpf.length() != 11) return false;
    if (cpf.matches("(\\d)\\1{10}")) return false;

    int soma1 = 0;
    for (int i = 0; i < 9; i++) {
      soma1 += (cpf.charAt(i) - '0') * (10 - i);
    }

    int digito1 = 11 - (soma1 % 11);
    if (digito1 >= 10) digito1 = 0;

    int soma2 = 0;
    for (int i = 0; i < 10; i++) {
      soma2 += (cpf.charAt(i) - '0') * (11 - i);
    }

    int digito2 = 11 - (soma2 % 11);
    if (digito2 >= 10) digito2 = 0;

    return digito1 == (cpf.charAt(9) - '0') && digito2 == (cpf.charAt(10) - '0');
  }
}
