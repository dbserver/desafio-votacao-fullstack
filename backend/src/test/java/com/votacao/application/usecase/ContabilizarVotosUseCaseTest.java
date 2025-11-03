package com.votacao.application.usecase;

import com.votacao.application.dto.ResultadoVotacaoResponseDTO;
import com.votacao.domain.model.Pauta;
import com.votacao.domain.model.SessaoVotacao;
import com.votacao.domain.service.SessaoVotacaoService;
import com.votacao.domain.service.VotoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ContabilizarVotosUseCaseTest {

    @Mock
    private SessaoVotacaoService sessaoVotacaoService;

    @Mock
    private VotoService votoService;

    @InjectMocks
    private ContabilizarVotosUseCase contabilizarVotosUseCase;

    @Test
    void deveContabilizarVotosCorretamente() {
        Pauta pauta = Pauta.builder().id(1L).titulo("Pauta").descricao("Desc").build();
        SessaoVotacao sessao = SessaoVotacao.builder()
                .id(1L)
                .pauta(pauta)
                .aberta(true)
                .build();

        when(sessaoVotacaoService.findById(1L)).thenReturn(sessao);
        when(votoService.contarVotosSim(sessao)).thenReturn(10L);
        when(votoService.contarVotosNao(sessao)).thenReturn(5L);

        ResultadoVotacaoResponseDTO resultado = contabilizarVotosUseCase.execute(1L);

        assertEquals(10L, resultado.getVotosSim());
        assertEquals(5L, resultado.getVotosNao());
        assertEquals(15L, resultado.getTotalVotos());
        assertEquals(pauta.getId(), resultado.getIdPauta());
    }

}