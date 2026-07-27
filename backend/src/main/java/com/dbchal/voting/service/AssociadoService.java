package com.dbchal.voting.service;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dbchal.voting.domain.entity.Associado;
import com.dbchal.voting.dto.associado.AssociadoResponse;
import com.dbchal.voting.dto.associado.CreateAssociadoRequest;
import com.dbchal.voting.exception.CreateEntityException;
import com.dbchal.voting.exception.associado.AssociadoNotFoundException;
import com.dbchal.voting.exception.associado.CpfAlreadyRegisteredException;
import com.dbchal.voting.repository.AssociadoRepository;

@Service
public class AssociadoService {

	private final AssociadoRepository associadoRepository;

	public AssociadoService(AssociadoRepository associadoRepository) {
		this.associadoRepository = associadoRepository;
	}

	@Transactional
	public AssociadoResponse create(CreateAssociadoRequest request) {
		if (associadoRepository.existsByCpf(request.cpf())) {
			throw new CpfAlreadyRegisteredException(request.cpf());
		}

		Associado associado = new Associado(request.cpf());

		try {
			Associado associadoSalvo = associadoRepository.saveAndFlush(associado);

			return AssociadoResponse.from(associadoSalvo);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw new CreateEntityException("associado");
		}
	}

	@Transactional(readOnly = true)
	public List<AssociadoResponse> findAll() {
		return associadoRepository
				.findAll(Sort.by(Sort.Direction.ASC, "id"))
				.stream()
				.map(AssociadoResponse::from)
				.toList();
	}

	@Transactional(readOnly = true)
	public AssociadoResponse findById(Long id) {
		return AssociadoResponse.from(findEntityById(id));
	}

	@Transactional(readOnly = true)
	public Associado findEntityById(Long id) {
		return associadoRepository.findById(id).orElseThrow(() -> new AssociadoNotFoundException(id));
	}

}
