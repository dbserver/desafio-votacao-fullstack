package com.votacao.backend.domain.services;

import com.votacao.backend.infrastructure.cpf.CpfStatus;
import com.votacao.backend.infrastructure.cpf.CpfValidationClient;
import com.votacao.backend.infrastructure.exception.CpfNaoAutorizadoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CpfValidationService {

    private final CpfValidationClient cpfValidationClient;

    public CpfStatus consultarStatus(String cpf) {
        return cpfValidationClient.validar(cpf);
    }

    public void garantirPodeVotar(String cpf) {
        CpfStatus status = consultarStatus(cpf);
        if (status != CpfStatus.ABLE_TO_VOTE) {
            throw new CpfNaoAutorizadoException();
        }
    }
}
