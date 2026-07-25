package com.dbchal.voting.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dbchal.voting.domain.Pauta;
import com.dbchal.voting.dto.CreatePautaRequest;
import com.dbchal.voting.dto.PautaResponse;
import com.dbchal.voting.repository.PautaRepository;

@Service
public class PautaService {

	private final PautaRepository pautaRepository;

	protected PautaService(PautaRepository pautaRepository) {
		this.pautaRepository = pautaRepository;
	}

	@Transactional(readOnly = true)
	public List<PautaResponse> findAll() {
		return pautaRepository.findAll().stream().map(PautaResponse::from).toList();
	}

	@Transactional(readOnly = true)
	public PautaResponse findById(Long id) {
		Pauta pauta = pautaRepository.findById(id).orElseThrow(() -> new RuntimeException("Pauta não encontrada"));

		return PautaResponse.from(pauta);
	}

	@Transactional
	public PautaResponse create(CreatePautaRequest request) {
		Pauta novaPauta = new Pauta(request.titulo(), request.descricao());

		Pauta pautaSalva = pautaRepository.save(novaPauta);

		return PautaResponse.from(pautaSalva);
	}
}
