package com.dbchal.voting.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dbchal.voting.domain.entity.Associado;
import com.dbchal.voting.domain.entity.SessaoVotacao;
import com.dbchal.voting.domain.entity.Voto;
import com.dbchal.voting.dto.voto.CreateVotoRequest;
import com.dbchal.voting.dto.voto.VotoResponse;
import com.dbchal.voting.exception.CreateEntityException;
import com.dbchal.voting.exception.sessao.ExpiredSessaoException;
import com.dbchal.voting.exception.voto.DuplicateVoteException;
import com.dbchal.voting.repository.VotoRepository;

@Service
public class VotoService {

	private final VotoRepository votoRepository;
	private final SessaoVotacaoService sessaoVotacaoService;
	private final AssociadoService associadoService;

	public VotoService(VotoRepository votoRepository, SessaoVotacaoService sessaoVotacaoService,
			AssociadoService associadoService) {
		this.votoRepository = votoRepository;
		this.sessaoVotacaoService = sessaoVotacaoService;
		this.associadoService = associadoService;
	}

	@Transactional
	public VotoResponse votar(Long pautaId, CreateVotoRequest request) {
		SessaoVotacao sessao = sessaoVotacaoService.findEntityByPauta(pautaId);

		if (!sessao.isOpen()) {
			throw new ExpiredSessaoException(pautaId);
		}

		Associado associado = associadoService.findEntityById(request.associadoId());

		if (votoRepository.existsBySessaoIdAndAssociadoId(sessao.getId(), associado.getId())) {
			throw new DuplicateVoteException();
		}

		Voto voto = new Voto(sessao, associado, request.opcao());

		try {
			Voto votoSalvo = votoRepository.saveAndFlush(voto);

			return VotoResponse.from(votoSalvo);
		} catch (DataIntegrityViolationException exception) {
			throw new CreateEntityException("voto");
		}
	}
}