package com.dbserver.votacao.client;

import com.dbserver.votacao.dto.CpfValidationResponse;
import com.dbserver.votacao.exception.BusinessException;
import com.dbserver.votacao.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class CpfValidationClient {

    private static final Logger log = LoggerFactory.getLogger(CpfValidationClient.class);
    private final SecureRandom random = new SecureRandom();

    public CpfValidationResponse validarCpf(String cpf) {
        log.info("Validando CPF do associado");

        String cpfNumeros = cpf.replaceAll("[^0-9]", "");

        if (!isCpfFormatoValido(cpfNumeros)) {
            log.warn("CPF com formato inválido");
            throw new NotFoundException("CPF inválido");
        }

        boolean podeVotar = random.nextBoolean();
        String status = podeVotar ? "ABLE_TO_VOTE" : "UNABLE_TO_VOTE";
        log.info("Resultado validação CPF: {}", status);

        if (!podeVotar) {
            throw new BusinessException("Associado não habilitado para votar (UNABLE_TO_VOTE)");
        }

        return new CpfValidationResponse(status);
    }

    private boolean isCpfFormatoValido(String cpf) {
        if (cpf.length() != 11) {
            return false;
        }

        if (cpf.chars().distinct().count() == 1) {
            return false;
        }

        int soma = 0;
        for (int i = 0; i < 9; i++) {
            soma += Character.getNumericValue(cpf.charAt(i)) * (10 - i);
        }
        int primeiroDigito = 11 - (soma % 11);
        if (primeiroDigito >= 10) {
            primeiroDigito = 0;
        }
        if (Character.getNumericValue(cpf.charAt(9)) != primeiroDigito) {
            return false;
        }

        soma = 0;
        for (int i = 0; i < 10; i++) {
            soma += Character.getNumericValue(cpf.charAt(i)) * (11 - i);
        }
        int segundoDigito = 11 - (soma % 11);
        if (segundoDigito >= 10) {
            segundoDigito = 0;
        }

        return Character.getNumericValue(cpf.charAt(10)) == segundoDigito;
    }
}
