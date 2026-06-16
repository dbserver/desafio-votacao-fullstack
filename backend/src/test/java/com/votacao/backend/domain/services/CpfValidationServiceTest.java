package com.votacao.backend.domain.services;

import com.votacao.backend.infrastructure.cpf.CpfStatus;
import com.votacao.backend.infrastructure.cpf.CpfValidationClient;
import com.votacao.backend.infrastructure.exception.CpfNaoAutorizadoException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CpfValidationServiceTest {

    @Mock
    private CpfValidationClient cpfValidationClient;

    @InjectMocks
    private CpfValidationService cpfValidationService;

    @Test
    void devePermitirCpfHabilitadoParaVotar() {
        when(cpfValidationClient.validar("12345678901")).thenReturn(CpfStatus.ABLE_TO_VOTE);

        assertDoesNotThrow(() -> cpfValidationService.garantirPodeVotar("12345678901"));
    }

    @Test
    void deveBloquearCpfNaoAutorizado() {
        when(cpfValidationClient.validar("12345678901")).thenReturn(CpfStatus.UNABLE_TO_VOTE);

        assertThrows(CpfNaoAutorizadoException.class, () -> cpfValidationService.garantirPodeVotar("12345678901"));
    }
}
