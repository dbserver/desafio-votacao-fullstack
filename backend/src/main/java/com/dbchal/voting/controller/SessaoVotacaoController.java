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

import com.dbchal.voting.dto.sessao.CreateSessaoVotacaoRequest;
import com.dbchal.voting.dto.sessao.SessaoVotacaoResponse;
import com.dbchal.voting.service.SessaoVotacaoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/sessoes")
@Tag(name = "Sessões de votação", description = "Abertura, consulta e exclusão de sessões de votação")
public class SessaoVotacaoController {

	private final SessaoVotacaoService sessaoVotacaoService;

	public SessaoVotacaoController(SessaoVotacaoService sessaoVotacaoService) {
		this.sessaoVotacaoService = sessaoVotacaoService;
	}

	@PostMapping("/{pautaId}")
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "Abrir sessão de votação", description = "Abre uma sessão para a pauta informada. "
			+ "Se a duração não for enviada, será utilizado o " + "tempo padrão de 1 minuto")
	@ApiResponses({
			@ApiResponse(responseCode = "201", description = "Sessão aberta com sucesso", content = @Content(schema = @Schema(implementation = SessaoVotacaoResponse.class))),
			@ApiResponse(responseCode = "400", description = "Duração da sessão inválida"),
			@ApiResponse(responseCode = "404", description = "Pauta não encontrada"),
			@ApiResponse(responseCode = "409", description = "A pauta já possui uma sessão de votação") })
	public SessaoVotacaoResponse openSession(
			@Parameter(description = "Identificador da pauta", example = "1", required = true) @PathVariable Long pautaId,

			@RequestBody(required = false) @Valid CreateSessaoVotacaoRequest request) {
		return sessaoVotacaoService.openSession(pautaId, request);
	}

	@GetMapping("/{pautaId}")
	@Operation(summary = "Consultar sessão por pauta", description = "Busca a sessão de votação vinculada à pauta")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Sessão encontrada", content = @Content(schema = @Schema(implementation = SessaoVotacaoResponse.class))),
			@ApiResponse(responseCode = "404", description = "Pauta ou sessão de votação não encontrada") })
	public SessaoVotacaoResponse findByPauta(
			@Parameter(description = "Identificador da pauta", example = "1", required = true) @PathVariable Long pautaId) {
		return sessaoVotacaoService.findByPauta(pautaId);
	}

	@DeleteMapping("/{pautaId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Operation(summary = "Excluir sessão de votação", description = "Exclui a sessão de votação vinculada à pauta")
	@ApiResponses({ @ApiResponse(responseCode = "204", description = "Sessão excluída com sucesso"),
			@ApiResponse(responseCode = "404", description = "Pauta ou sessão de votação não encontrada") })
	public void deleteByPauta(
			@Parameter(description = "Identificador da pauta", example = "1", required = true) @PathVariable Long pautaId) {
		sessaoVotacaoService.deleteByPauta(pautaId);
	}
}