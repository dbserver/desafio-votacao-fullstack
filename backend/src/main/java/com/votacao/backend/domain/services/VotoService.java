package com.votacao.backend.domain.services;

import com.votacao.backend.api.v1.dto.ResultadoResponse;
import com.votacao.backend.domain.entity.Sessao;
import com.votacao.backend.domain.entity.Voto;
import com.votacao.backend.domain.enums.OpcaoVoto;
import com.votacao.backend.domain.repository.VotoRepository;
import com.votacao.backend.infrastructure.exception.VotoNaoEncontradoException;
import com.votacao.backend.infrastructure.exception.VotoDuplicadoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class VotoService {

    private final VotoRepository votoRepository;
    private final SessaoService sessaoService;
    private final CpfValidationService cpfValidationService;
    private final SimpMessagingTemplate messagingTemplate;

    @Transactional
    public Voto registrar(UUID sessaoId, String associadoId, OpcaoVoto opcao) {
        Sessao sessao = sessaoService.buscarAtiva(sessaoId);
        cpfValidationService.garantirPodeVotar(associadoId);

        if (votoRepository.existsBySessaoIdAndAssociadoId(sessaoId, associadoId)) {
            throw new VotoDuplicadoException();
        }

        Voto voto = new Voto();
        voto.setSessao(sessao);
        voto.setAssociadoId(associadoId);
        voto.setOpcao(opcao);

        Voto salvo = votoRepository.save(voto);

        ResultadoResponse resultado = resultado(sessaoId);
        messagingTemplate.convertAndSend("/topic/resultado/" + sessaoId, resultado);
        log.info("Resultado publicado via WebSocket para sessão {}", sessaoId);

        return salvo;
    }

    @Transactional(readOnly = true)
    public ResultadoResponse resultado(UUID sessaoId) {
        Sessao sessao = sessaoService.buscarPorId(sessaoId);
        long sim = votoRepository.countBySessaoIdAndOpcao(sessaoId, OpcaoVoto.SIM);
        long nao = votoRepository.countBySessaoIdAndOpcao(sessaoId, OpcaoVoto.NAO);
        return new ResultadoResponse(
                sessao.getPauta().getId(),
                sessaoId,
                sim,
                nao,
                sim + nao,
                !sessao.isAtiva()
        );
    }

    @Transactional(readOnly = true)
    public Voto buscarPorId(UUID sessaoId, UUID votoId) {
        return votoRepository.findByIdAndSessaoId(votoId, sessaoId)
                .orElseThrow(() -> new VotoNaoEncontradoException(votoId));
    }
}
