package com.dbchal.voting.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.dbchal.voting.dto.voto.CreateVotoRequest;
import com.dbchal.voting.dto.voto.VotoResponse;
import com.dbchal.voting.service.VotoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/votos")
public class VotoController {
	private final VotoService votoService;

	public VotoController(VotoService votoService) {
		this.votoService = votoService;
	}

	@PostMapping("/{pautaId}")
	@ResponseStatus(HttpStatus.CREATED)
	public VotoResponse votar(@PathVariable Long pautaId, @RequestBody @Valid CreateVotoRequest request) {
		return votoService.votar(pautaId, request);
	}
}
