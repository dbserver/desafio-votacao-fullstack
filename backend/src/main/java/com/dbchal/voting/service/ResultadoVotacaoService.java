package com.dbchal.voting.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dbchal.voting.domain.entity.SessaoVotacao;
import com.dbchal.voting.domain.enums.ResultadoVotacaoStatus;
import com.dbchal.voting.dto.voto.ResultadoVotacaoResponse;
import com.dbchal.voting.dto.voto.ResultadoVotosProjection;
import com.dbchal.voting.repository.VotoRepository;

@Service
public class ResultadoVotacaoService {

	private final VotoRepository votoRepository;
	private final SessaoVotacaoService sessaoVotacaoService;

	public ResultadoVotacaoService(VotoRepository votoRepository, SessaoVotacaoService sessaoVotacaoService) {
		this.votoRepository = votoRepository;
		this.sessaoVotacaoService = sessaoVotacaoService;
	}

	@Transactional(readOnly = true)
	public ResultadoVotacaoResponse getResult(Long pautaId) {
		SessaoVotacao sessao = sessaoVotacaoService.findEntityByPauta(pautaId);

		ResultadoVotosProjection contagem = votoRepository.countBySessaoId(sessao.getId());

		long votosSim = contagem.getVotosSim() == null ? 0 : contagem.getVotosSim();
		long votosNao = contagem.getVotosNao() == null ? 0 : contagem.getVotosNao();

		long totalVotos = votosSim + votosNao;
		boolean sessaoAberta = sessao.isOpen();

		ResultadoVotacaoStatus resultado = determineResult(sessaoAberta, votosSim, votosNao);

		return new ResultadoVotacaoResponse(pautaId, sessao.getId(), votosSim, votosNao, totalVotos, sessaoAberta,
				resultado);
	}

	private ResultadoVotacaoStatus determineResult(boolean sessaoAberta, long votosSim, long votosNao) {
		if (sessaoAberta) {
		    return ResultadoVotacaoStatus.EM_ANDAMENTO;
		}

		if (votosSim == 0 && votosNao == 0) {
		    return ResultadoVotacaoStatus.SEM_VOTOS;
		}

		if (votosSim > votosNao) {
		    return ResultadoVotacaoStatus.APROVADA;
		}

		if (votosNao > votosSim) {
		    return ResultadoVotacaoStatus.REJEITADA;
		}
		
		return ResultadoVotacaoStatus.EMPATE;

	}

}
