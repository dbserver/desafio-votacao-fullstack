package com.votacao.application.usecase;

import com.votacao.domain.model.Pauta;
import com.votacao.domain.model.SessaoVotacao;
import com.votacao.domain.service.SessaoVotacaoService;
import com.votacao.domain.service.VotoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegistrarVotoUseCaseTest {

    @Mock
    private VotoService votoService;

    @Mock
    private SessaoVotacaoService sessaoVotacaoService;

    @InjectMocks
    private RegistrarVotoUseCase registrarVotoUseCase;

    @Test
    void deveRegistrarVoto() {
        SessaoVotacao sessao = SessaoVotacao.builder()
                .id(1L)
                .pauta(Pauta.builder().id(1L).build())
                .build();

        when(sessaoVotacaoService.findById(1L)).thenReturn(sessao);

        registrarVotoUseCase.execute(1L, "Sim");

        verify(votoService, times(1)).save(argThat(voto -> voto.getSessaoVotacao().equals(sessao) && voto.getEscolha().equals("Sim") && voto.isVoto()));
    }
}