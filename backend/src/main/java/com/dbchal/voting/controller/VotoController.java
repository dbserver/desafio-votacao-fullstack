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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/votos")
@Tag(name = "Votos", description = "Registro de votos em pautas")
public class VotoController {

	private final VotoService votoService;

	public VotoController(VotoService votoService) {
		this.votoService = votoService;
	}

	@PostMapping("/{pautaId}")
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "Registrar voto", description = "Registra o voto de um associado na sessão aberta "
			+ "da pauta informada")
	@ApiResponses({
			@ApiResponse(responseCode = "201", description = "Voto registrado com sucesso", content = @Content(schema = @Schema(implementation = VotoResponse.class))),
			@ApiResponse(responseCode = "400", description = "Dados do voto inválidos"),
			@ApiResponse(responseCode = "403", description = "A sessão de votação está encerrada"),
			@ApiResponse(responseCode = "404", description = "Pauta, sessão ou associado não encontrado"),
			@ApiResponse(responseCode = "409", description = "O associado já votou nesta sessão") })
	public VotoResponse votar(
			@Parameter(description = "Identificador da pauta", example = "1", required = true) @PathVariable Long pautaId,

			@RequestBody @Valid CreateVotoRequest request) {
		return votoService.votar(pautaId, request);
	}
}