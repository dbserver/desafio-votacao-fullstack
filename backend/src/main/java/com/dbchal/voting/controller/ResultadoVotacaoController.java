package com.dbchal.voting.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dbchal.voting.dto.voto.ResultadoVotacaoResponse;
import com.dbchal.voting.service.ResultadoVotacaoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/resultados")
@Tag(name = "Resultados", description = "Consulta dos resultados das votações")
public class ResultadoVotacaoController {

	private final ResultadoVotacaoService resultadoVotacaoService;

	public ResultadoVotacaoController(ResultadoVotacaoService resultadoVotacaoService) {
		this.resultadoVotacaoService = resultadoVotacaoService;
	}

	@GetMapping("/{pautaId}")
	@Operation(summary = "Consultar resultado da votação", description = "Retorna a quantidade de votos SIM e NÃO e o "
			+ "resultado atual da pauta")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Resultado consultado com sucesso", content = @Content(schema = @Schema(implementation = ResultadoVotacaoResponse.class))),
			@ApiResponse(responseCode = "404", description = "Pauta ou sessão de votação não encontrada") })
	public ResultadoVotacaoResponse getResult(
			@Parameter(description = "Identificador da pauta", example = "1", required = true) @PathVariable Long pautaId) {
		return resultadoVotacaoService.getResult(pautaId);
	}
}