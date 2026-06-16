package com.votacao.backend.infrastructure.scheduler;

import com.votacao.backend.api.v1.dto.ResultadoResponse;
import com.votacao.backend.domain.entity.Sessao;
import com.votacao.backend.domain.repository.SessaoRepository;
import com.votacao.backend.domain.services.SessaoService;
import com.votacao.backend.domain.services.VotoService;
import com.votacao.backend.infrastructure.time.AppDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class SessaoEncerramentoScheduler {

    private final SessaoRepository sessaoRepository;
    private final SessaoService sessaoService;
    private final VotoService votoService;
    private final SimpMessagingTemplate messagingTemplate;

    @Scheduled(fixedDelayString = "${votacao.sessao.verificacao-ms:1000}")
    public void encerrarSessoesExpiradas() {
        List<Sessao> sessoesExpiradas = sessaoRepository.findByEncerradaEmIsNullAndExpiraEmLessThanEqual(AppDateTime.now());

        for (Sessao sessao : sessoesExpiradas) {
            Sessao encerrada = sessaoService.encerrar(sessao);
            ResultadoResponse resultado = votoService.resultado(encerrada.getId());
            messagingTemplate.convertAndSend("/topic/resultado/" + encerrada.getId(), resultado);
            log.info("Sessão {} encerrada automaticamente por expiração", encerrada.getId());
        }
    }
}
