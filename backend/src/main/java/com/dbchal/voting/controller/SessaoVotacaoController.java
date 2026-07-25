package com.dbchal.voting.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.dbchal.voting.dto.OpenSessaoVotacaoRequest;
import com.dbchal.voting.dto.SessaoVotacaoResponse;
import com.dbchal.voting.service.SessaoVotacaoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/sessoes")
public class SessaoVotacaoController {
	private final SessaoVotacaoService sessaoVotacaoService;

	public SessaoVotacaoController(SessaoVotacaoService sessaoVotacaoService) {
		this.sessaoVotacaoService = sessaoVotacaoService;
	}

	@PostMapping("/{pautaId}")
	@ResponseStatus(HttpStatus.CREATED)
	public SessaoVotacaoResponse openSession(@PathVariable Long pautaId,
			@RequestBody(required = false) @Valid OpenSessaoVotacaoRequest request) {
		return sessaoVotacaoService.openSession(pautaId, request);
	}

	@GetMapping("/{pautaId}")
	@ResponseStatus(HttpStatus.FOUND)
	public SessaoVotacaoResponse findByPauta(@PathVariable Long pautaId) {
		return sessaoVotacaoService.findByPauta(pautaId);
	}

	@DeleteMapping("/{pautaId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteByPauta(@PathVariable Long pautaId) {
		sessaoVotacaoService.deleteByPauta(pautaId);
	}

}
