package com.dbchal.voting.service;

import java.time.Instant;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dbchal.voting.domain.entity.Pauta;
import com.dbchal.voting.domain.entity.SessaoVotacao;
import com.dbchal.voting.dto.sessao.CreateSessaoVotacaoRequest;
import com.dbchal.voting.dto.sessao.SessaoVotacaoResponse;
import com.dbchal.voting.exception.CreateEntityException;
import com.dbchal.voting.exception.sessao.AlreadyExistingSessaoException;
import com.dbchal.voting.exception.sessao.SessaoNotFoundException;
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
	public SessaoVotacaoResponse openSession(Long pautaId, CreateSessaoVotacaoRequest request) {

		Pauta pauta = pautaService.findEntityById(pautaId);

		if (sessaoVotacaoRepository.existsByPautaId(pautaId)) {
			throw new AlreadyExistingSessaoException(pautaId);
		}

		long duracao = obterDuracao(request);

		Instant inicio = Instant.now();
		Instant fim = inicio.plusSeconds(duracao);

		SessaoVotacao sessao = new SessaoVotacao(pautaId, pauta, inicio, fim);

		try {

			SessaoVotacao sessaoSalva = sessaoVotacaoRepository.saveAndFlush(sessao);
			return SessaoVotacaoResponse.from(sessaoSalva);

		} catch (Exception e) {
			throw new CreateEntityException("sessão votação");
		}

	}

	@Transactional(readOnly = true)
	public SessaoVotacaoResponse findByPauta(Long pautaId) {
		SessaoVotacao sessao = sessaoVotacaoRepository.findByPautaId(pautaId)
				.orElseThrow(() -> new SessaoNotFoundException(pautaId));

		return SessaoVotacaoResponse.from(sessao);
	}

	@Transactional(readOnly = true)
	public SessaoVotacao findEntityByPauta(Long pautaId) {
		return sessaoVotacaoRepository.findByPautaId(pautaId).orElseThrow(() -> new SessaoNotFoundException(pautaId));
	}

	@Transactional
	public void deleteByPauta(Long pautaId) {
		SessaoVotacao sessao = sessaoVotacaoRepository.findByPautaId(pautaId)
				.orElseThrow(() -> new SessaoNotFoundException(pautaId));

		sessaoVotacaoRepository.delete(sessao);
	}

	private long obterDuracao(CreateSessaoVotacaoRequest request) {
		if (request == null || request.duracao() == null) {
			return DURACAO_PADRAO;
		}

		return request.duracao();
	}

}
