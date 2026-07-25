package com.dbchal.voting.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dbchal.voting.domain.entity.Pauta;
import com.dbchal.voting.dto.pauta.CreatePautaRequest;
import com.dbchal.voting.dto.pauta.PautaResponse;
import com.dbchal.voting.exception.CreateEntityException;
import com.dbchal.voting.exception.pauta.PautaNotFoundException;
import com.dbchal.voting.repository.PautaRepository;

@Service
public class PautaService {

	private final PautaRepository pautaRepository;

	public PautaService(PautaRepository pautaRepository) {
		this.pautaRepository = pautaRepository;
	}

	@Transactional
	public PautaResponse create(CreatePautaRequest request) {
		Pauta novaPauta = new Pauta(request.titulo(), request.descricao());

		try {
			Pauta pautaSalva = pautaRepository.save(novaPauta);

			return PautaResponse.from(pautaSalva);
		} catch (Exception e) {
			throw new CreateEntityException("pauta");
		}
	}

	@Transactional(readOnly = true)
	public List<PautaResponse> findAll() {
		return pautaRepository.findAll().stream().map(PautaResponse::from).toList();
	}

	@Transactional(readOnly = true)
	public PautaResponse findById(Long id) {
		Pauta pauta = pautaRepository.findById(id).orElseThrow(() -> new PautaNotFoundException(id));

		return PautaResponse.from(pauta);
	}

	@Transactional(readOnly = true)
	public Pauta findEntityById(Long id) {
		return pautaRepository.findById(id).orElseThrow(() -> new PautaNotFoundException(id));
	}
}
