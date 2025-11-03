package com.votacao.application.usecase;

import com.votacao.application.dto.SessaoVotacaoRequestDTO;
import com.votacao.domain.model.Pauta;
import com.votacao.domain.service.PautaService;
import com.votacao.domain.service.SessaoVotacaoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AbrirSessaoUseCaseTest {

    @Mock
    private SessaoVotacaoService sessaoVotacaoService;

    @Mock
    private PautaService pautaService;

    @InjectMocks
    private AbrirSessaoUseCase abrirSessaoUseCase;

    @Test
    void deveAbrirSessaoComDuracaoCalculada() {
        SessaoVotacaoRequestDTO dto = SessaoVotacaoRequestDTO.builder()
                .idPauta(1L)
                .inicioSessao(LocalDateTime.of(2025, 11, 2, 10, 0))
                .fimSessao(LocalDateTime.of(2025, 11, 2, 11, 0))
                .build();

        Pauta pauta = Pauta.builder().id(1L).titulo("Pauta").build();
        when(pautaService.findById(1L)).thenReturn(pauta);

        abrirSessaoUseCase.execute(dto);

        verify(sessaoVotacaoService, times(1)).save(argThat(sessao -> sessao.getPauta().equals(pauta) && sessao.getTempoDuracao() == 60L && !sessao.isAberta()));
    }

}