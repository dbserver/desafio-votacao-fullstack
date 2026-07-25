package com.dbchal.voting.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dbchal.voting.dto.voto.ResultadoVotacaoResponse;
import com.dbchal.voting.service.ResultadoVotacaoService;

@RestController
@RequestMapping("/api/v1/resultados")
public class ResultadoVotacaoController {

	private final ResultadoVotacaoService resultadoVotacaoService;

	public ResultadoVotacaoController(ResultadoVotacaoService resultadoVotacaoService) {
		this.resultadoVotacaoService = resultadoVotacaoService;
	}

	@GetMapping("/{pautaId}")
	public ResultadoVotacaoResponse getResult(@PathVariable Long pautaId) {
		return resultadoVotacaoService.getResult(pautaId);
	}

}
