package com.dbchal.voting.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dbchal.voting.dto.associado.AssociadoResponse;
import com.dbchal.voting.repository.AssociadoRepository;

@Service
public class AssociadoQueryService {

	private final AssociadoRepository associadoRepository;

	public AssociadoQueryService(AssociadoRepository associadoRepository) {
		this.associadoRepository = associadoRepository;
	}

	@Transactional(readOnly = true)
	public Page<AssociadoResponse> findAll(Pageable pageable) {
		return associadoRepository.findAll(pageable).map(AssociadoResponse::from);
	}
}
