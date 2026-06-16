package com.votacao.backend.support;

import com.votacao.backend.infrastructure.cpf.CpfStatus;
import com.votacao.backend.infrastructure.cpf.CpfValidationClient;
import com.votacao.backend.infrastructure.exception.CpfInvalidoException;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class MutableCpfValidationClient implements CpfValidationClient {

    private final Map<String, CpfStatus> statuses = new ConcurrentHashMap<>();
    private final Set<String> invalidos = ConcurrentHashMap.newKeySet();

    public void limpar() {
        statuses.clear();
        invalidos.clear();
    }

    public void marcarValido(String cpf, CpfStatus status) {
        invalidos.remove(cpf);
        statuses.put(cpf, status);
    }

    public void marcarInvalido(String cpf) {
        statuses.remove(cpf);
        invalidos.add(cpf);
    }

    @Override
    public CpfStatus validar(String cpf) {
        if (cpf == null || cpf.isBlank() || invalidos.contains(cpf)) {
            throw new CpfInvalidoException();
        }

        return statuses.getOrDefault(cpf, CpfStatus.ABLE_TO_VOTE);
    }
}
