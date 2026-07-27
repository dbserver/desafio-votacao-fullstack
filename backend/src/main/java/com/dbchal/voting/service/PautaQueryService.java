package com.dbchal.voting.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dbchal.voting.dto.pauta.PautaResponse;
import com.dbchal.voting.repository.PautaRepository;

@Service
public class PautaQueryService {

	private final PautaRepository pautaRepository;

	public PautaQueryService(PautaRepository pautaRepository) {
		this.pautaRepository = pautaRepository;
	}

	@Transactional(readOnly = true)
	public Page<PautaResponse> findAll(Pageable pageable) {
		return pautaRepository.findAll(pageable)
				.map(PautaResponse::from);
	}
}
