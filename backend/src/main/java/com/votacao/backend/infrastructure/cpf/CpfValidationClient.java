package com.votacao.backend.infrastructure.cpf;

public interface CpfValidationClient {
    CpfStatus validar(String cpf);
}
