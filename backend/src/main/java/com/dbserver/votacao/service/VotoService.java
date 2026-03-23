package com.dbserver.votacao.service;

import com.dbserver.votacao.client.CpfValidationClient;
import com.dbserver.votacao.dto.ResultadoVotacaoResponse;
import com.dbserver.votacao.dto.VotoRequest;
import com.dbserver.votacao.dto.VotoResponse;
import com.dbserver.votacao.entity.SessaoVotacao;
import com.dbserver.votacao.entity.Voto;
import com.dbserver.votacao.enums.VotoOpcao;
import com.dbserver.votacao.exception.BusinessException;
import com.dbserver.votacao.repository.VotoRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VotoService {

    private static final Logger log = LoggerFactory.getLogger(VotoService.class);

    private final VotoRepository votoRepository;
    private final SessaoVotacaoService sessaoVotacaoService;
    private final CpfValidationClient cpfValidationClient;

    public VotoResponse votar(Long pautaId, VotoRequest request) {
        log.info("Registrando voto do associado na pauta {}", pautaId);

        SessaoVotacao sessao = sessaoVotacaoService.findByPautaId(pautaId);

        if (!sessao.isAberta()) {
            throw new BusinessException("Sessão de votação não está aberta");
        }

        if (votoRepository.existsBySessaoIdAndAssociadoId(sessao.getId(), request.associadoId())) {
            throw new BusinessException("Associado já votou nesta pauta");
        }

        cpfValidationClient.validarCpf(request.associadoId());

        Voto voto = Voto.builder()
                .sessao(sessao)
                .associadoId(request.associadoId())
                .voto(request.voto())
                .build();

        Voto salvo = votoRepository.save(voto);
        log.info("Voto registrado com id: {}", salvo.getId());

        return new VotoResponse(salvo.getId(), salvo.getAssociadoId(), salvo.getVoto());
    }

    public ResultadoVotacaoResponse resultado(Long pautaId) {
        log.info("Contabilizando resultado da pauta: {}", pautaId);

        SessaoVotacao sessao = sessaoVotacaoService.findByPautaId(pautaId);

        long votosSim = votoRepository.countBySessaoIdAndVoto(sessao.getId(), VotoOpcao.SIM);
        long votosNao = votoRepository.countBySessaoIdAndVoto(sessao.getId(), VotoOpcao.NAO);
        long total = votosSim + votosNao;

        String resultado;
        if (votosSim > votosNao) {
            resultado = "APROVADA";
        } else if (votosNao > votosSim) {
            resultado = "REPROVADA";
        } else {
            resultado = "EMPATE";
        }

        log.info("Resultado pauta {}: {} (SIM={}, NAO={})", pautaId, resultado, votosSim, votosNao);

        return new ResultadoVotacaoResponse(
                pautaId,
                sessao.getPauta().getTitulo(),
                total,
                votosSim,
                votosNao,
                resultado
        );
    }
}
