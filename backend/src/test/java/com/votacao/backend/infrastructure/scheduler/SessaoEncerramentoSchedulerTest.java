package com.votacao.backend.infrastructure.scheduler;

import com.votacao.backend.api.v1.dto.ResultadoResponse;
import com.votacao.backend.domain.entity.Pauta;
import com.votacao.backend.domain.entity.Sessao;
import com.votacao.backend.domain.repository.SessaoRepository;
import com.votacao.backend.domain.services.SessaoService;
import com.votacao.backend.domain.services.VotoService;
import com.votacao.backend.infrastructure.time.AppDateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SessaoEncerramentoSchedulerTest {

    @Mock
    private SessaoRepository sessaoRepository;

    @Mock
    private SessaoService sessaoService;

    @Mock
    private VotoService votoService;

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    private SessaoEncerramentoScheduler scheduler;

    @Test
    void deveEncerrarSessaoExpiradaEPublicarResultado() {
        UUID pautaId = UUID.randomUUID();
        UUID sessaoId = UUID.randomUUID();

        Pauta pauta = new Pauta();
        pauta.setId(pautaId);

        Sessao sessao = new Sessao();
        sessao.setId(sessaoId);
        sessao.setPauta(pauta);
        sessao.setExpiraEm(AppDateTime.now().minusMinutes(1));

        ResultadoResponse resultado = new ResultadoResponse(pautaId, sessaoId, 3, 1, 4, true);

        when(sessaoRepository.findByEncerradaEmIsNullAndExpiraEmLessThanEqual(any(LocalDateTime.class))).thenReturn(List.of(sessao));
        when(sessaoService.encerrar(sessao)).thenAnswer(invocation -> {
            Sessao encerrada = invocation.getArgument(0);
            encerrada.setEncerradaEm(AppDateTime.now());
            return encerrada;
        });
        when(votoService.resultado(sessaoId)).thenReturn(resultado);

        scheduler.encerrarSessoesExpiradas();

        verify(sessaoService).encerrar(sessao);
        verify(votoService).resultado(sessaoId);
        verify(messagingTemplate).convertAndSend("/topic/resultado/" + sessaoId, resultado);
    }
}
