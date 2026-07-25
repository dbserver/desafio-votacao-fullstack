package com.dbchal.voting.service;

import java.time.Instant;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dbchal.voting.domain.Pauta;
import com.dbchal.voting.domain.SessaoVotacao;
import com.dbchal.voting.dto.OpenSessaoVotacaoRequest;
import com.dbchal.voting.dto.SessaoVotacaoResponse;
import com.dbchal.voting.exception.AlreadyExistingSessionException;
import com.dbchal.voting.exception.SessaoNotFoundException;
import com.dbchal.voting.repository.SessaoVotacaoRepository;

@Service
public class SessaoVotacaoService {

	private static final long DURACAO_PADRAO = 60L;

	private final SessaoVotacaoRepository sessaoVotacaoRepository;
	private final PautaService pautaService;

	public SessaoVotacaoService(SessaoVotacaoRepository sessaoVotacaoRepository, PautaService pautaService) {
		this.sessaoVotacaoRepository = sessaoVotacaoRepository;
		this.pautaService = pautaService;
	}

	@Transactional
	public SessaoVotacaoResponse openSession(Long pautaId, OpenSessaoVotacaoRequest request) {

		Pauta pauta = pautaService.findEntityById(pautaId);

		if (sessaoVotacaoRepository.existsByPautaId(pautaId)) {
			throw new AlreadyExistingSessionException(pautaId);
		}

		long duracao = obterDuracao(request);

		Instant inicio = Instant.now();
		Instant fim = inicio.plusSeconds(duracao);

		SessaoVotacao sessao = new SessaoVotacao(pautaId, pauta, inicio, fim);

		try {

			SessaoVotacao sessaoSalva = sessaoVotacaoRepository.saveAndFlush(sessao);
			return SessaoVotacaoResponse.from(sessaoSalva);

		} catch (Exception e) {
			throw new AlreadyExistingSessionException(pautaId);
		}

	}

	@Transactional(readOnly = true)
	public SessaoVotacaoResponse findByPauta(Long puataId) {
		SessaoVotacao sessao = sessaoVotacaoRepository.findByPautaId(puataId)
				.orElseThrow(() -> new SessaoNotFoundException(puataId));

		return SessaoVotacaoResponse.from(sessao);
	}
	
	@Transactional
	public void deleteByPauta(Long pautaId) {
		SessaoVotacao sessao = sessaoVotacaoRepository.findByPautaId(pautaId).orElseThrow(() -> new SessaoNotFoundException(pautaId));
		
		sessaoVotacaoRepository.delete(sessao);
	}

	private long obterDuracao(OpenSessaoVotacaoRequest request) {
		if (request == null || request.duracao() == null) {
			return DURACAO_PADRAO;
		}

		return request.duracao();
	}

}
