package com.dbserver.votacao.service;

import com.dbserver.votacao.dto.SessaoVotacaoRequest;
import com.dbserver.votacao.dto.SessaoVotacaoResponse;
import com.dbserver.votacao.entity.Pauta;
import com.dbserver.votacao.entity.SessaoVotacao;
import com.dbserver.votacao.exception.BusinessException;
import com.dbserver.votacao.exception.NotFoundException;
import com.dbserver.votacao.repository.SessaoVotacaoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SessaoVotacaoServiceTest {

    @Mock
    private SessaoVotacaoRepository sessaoRepository;

    @Mock
    private PautaService pautaService;

    @InjectMocks
    private SessaoVotacaoService sessaoVotacaoService;

    private Pauta criarPauta() {
        return Pauta.builder().id(1L).titulo("Pauta Teste").criadaEm(LocalDateTime.now()).build();
    }

    @Test
    void deveAbrirSessaoComDuracaoPadrao() {
        Pauta pauta = criarPauta();
        SessaoVotacao sessao = SessaoVotacao.builder()
                .id(1L)
                .pauta(pauta)
                .dataInicio(LocalDateTime.now())
                .dataFim(LocalDateTime.now().plusMinutes(1))
                .build();

        when(pautaService.findById(1L)).thenReturn(pauta);
        when(sessaoRepository.existsByPautaId(1L)).thenReturn(false);
        when(sessaoRepository.save(any(SessaoVotacao.class))).thenReturn(sessao);

        SessaoVotacaoRequest request = new SessaoVotacaoRequest(1L, null);
        SessaoVotacaoResponse response = sessaoVotacaoService.abrirSessao(request);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.pautaId()).isEqualTo(1L);
    }

    @Test
    void deveAbrirSessaoComDuracaoCustomizada() {
        Pauta pauta = criarPauta();
        SessaoVotacao sessao = SessaoVotacao.builder()
                .id(1L)
                .pauta(pauta)
                .dataInicio(LocalDateTime.now())
                .dataFim(LocalDateTime.now().plusMinutes(10))
                .build();

        when(pautaService.findById(1L)).thenReturn(pauta);
        when(sessaoRepository.existsByPautaId(1L)).thenReturn(false);
        when(sessaoRepository.save(any(SessaoVotacao.class))).thenReturn(sessao);

        SessaoVotacaoRequest request = new SessaoVotacaoRequest(1L, 10);
        SessaoVotacaoResponse response = sessaoVotacaoService.abrirSessao(request);

        assertThat(response.id()).isEqualTo(1L);
    }

    @Test
    void deveLancarExcecaoQuandoSessaoJaExiste() {
        Pauta pauta = criarPauta();
        when(pautaService.findById(1L)).thenReturn(pauta);
        when(sessaoRepository.existsByPautaId(1L)).thenReturn(true);

        SessaoVotacaoRequest request = new SessaoVotacaoRequest(1L, null);

        assertThatThrownBy(() -> sessaoVotacaoService.abrirSessao(request))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Já existe uma sessão");
    }

    @Test
    void deveLancarExcecaoQuandoSessaoNaoEncontrada() {
        when(sessaoRepository.findByPautaId(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> sessaoVotacaoService.buscarPorPautaId(99L))
                .isInstanceOf(NotFoundException.class);
    }
}
