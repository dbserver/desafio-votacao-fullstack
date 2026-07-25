package com.dbchal.voting.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.dbchal.voting.dto.associado.AssociadoResponse;
import com.dbchal.voting.dto.associado.CreateAssociadoRequest;
import com.dbchal.voting.service.AssociadoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/associados")
public class AssociadoController {
	private final AssociadoService associadoService;

	public AssociadoController(AssociadoService associadoService) {
		this.associadoService = associadoService;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public AssociadoResponse criar(@RequestBody @Valid CreateAssociadoRequest request) {
		return associadoService.create(request);
	}

	@GetMapping("/{id}")
	public AssociadoResponse buscarPorId(@PathVariable Long id) {
		return associadoService.findById(id);
	}
}
