package com.votacao.backend.domain.services;

import com.votacao.backend.domain.entity.Pauta;
import com.votacao.backend.domain.entity.Sessao;
import com.votacao.backend.domain.repository.SessaoRepository;
import com.votacao.backend.infrastructure.exception.SessaoExpiradaException;
import com.votacao.backend.infrastructure.exception.SessaoNaoEncontradaException;
import com.votacao.backend.infrastructure.time.AppDateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SessaoServiceTest {

    @Mock
    private SessaoRepository sessaoRepository;

    @Mock
    private PautaService pautaService;

    @InjectMocks
    private SessaoService sessaoService;

    @Test
    void deveAbrirSessaoComDuracaoPadraoQuandoDuracaoForInvalida() {
        UUID pautaId = UUID.randomUUID();
        Pauta pauta = new Pauta();
        pauta.setId(pautaId);

        when(pautaService.buscarPorId(pautaId)).thenReturn(pauta);
        when(sessaoRepository.findByPautaId(pautaId)).thenReturn(Optional.empty());
        when(sessaoRepository.save(any(Sessao.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Sessao sessao = sessaoService.abrir(pautaId, 0);

        assertEquals(pautaId, sessao.getPauta().getId());
        long segundos = Duration.between(sessao.getAbertaEm(), sessao.getExpiraEm()).toSeconds();
        assertTrue(segundos >= 59 && segundos <= 61);
    }

    @Test
    void deveLancarNaoEncontradaQuandoSessaoNaoExistir() {
        UUID sessaoId = UUID.randomUUID();

        when(sessaoRepository.findById(sessaoId)).thenReturn(Optional.empty());

        assertThrows(SessaoNaoEncontradaException.class, () -> sessaoService.buscarAtiva(sessaoId));
    }

    @Test
    void deveLancarExpiradaQuandoSessaoNaoEstiverAtiva() {
        UUID sessaoId = UUID.randomUUID();
        Sessao sessao = new Sessao();
        sessao.setId(sessaoId);
        sessao.setExpiraEm(AppDateTime.now().minusMinutes(1));

        when(sessaoRepository.findById(sessaoId)).thenReturn(Optional.of(sessao));

        assertThrows(SessaoExpiradaException.class, () -> sessaoService.buscarAtiva(sessaoId));
    }
}
