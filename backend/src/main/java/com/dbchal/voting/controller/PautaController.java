package com.dbchal.voting.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.dbchal.voting.dto.CreatePautaRequest;
import com.dbchal.voting.dto.PautaResponse;
import com.dbchal.voting.service.PautaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/pautas")
public class PautaController {

	private final PautaService pautaService;

	public PautaController(PautaService pautaService) {
		this.pautaService = pautaService;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public PautaResponse create(@RequestBody @Valid CreatePautaRequest request) {
		return pautaService.create(request);
	}

	@GetMapping
	@ResponseStatus(HttpStatus.FOUND)
	public List<PautaResponse> listAll() {
		return pautaService.findAll();
	}

	@GetMapping("/{id}")
	@ResponseStatus(HttpStatus.FOUND)
	public PautaResponse findById(@PathVariable Long id) {
		return pautaService.findById(id);
	}

}
