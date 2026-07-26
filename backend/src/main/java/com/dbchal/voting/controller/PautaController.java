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

import com.dbchal.voting.dto.pauta.CreatePautaRequest;
import com.dbchal.voting.dto.pauta.PautaResponse;
import com.dbchal.voting.service.PautaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/pautas")
@Tag(name = "Pautas", description = "Cadastro e consulta de pautas")
public class PautaController {

	private final PautaService pautaService;

	public PautaController(PautaService pautaService) {
		this.pautaService = pautaService;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "Cadastrar pauta", description = "Cadastra uma nova pauta para votação")
	@ApiResponses({
			@ApiResponse(responseCode = "201", description = "Pauta cadastrada com sucesso", content = @Content(schema = @Schema(implementation = PautaResponse.class))),
			@ApiResponse(responseCode = "400", description = "Dados da requisição inválidos") })
	public PautaResponse create(@RequestBody @Valid CreatePautaRequest request) {
		return pautaService.create(request);
	}

	@GetMapping
	@Operation(summary = "Listar pautas", description = "Retorna todas as pautas cadastradas")
	@ApiResponse(responseCode = "200", description = "Pautas encontradas", content = @Content(array = @ArraySchema(schema = @Schema(implementation = PautaResponse.class))))
	public List<PautaResponse> listAll() {
		return pautaService.findAll();
	}

	@GetMapping("/{id}")
	@Operation(summary = "Consultar pauta", description = "Busca uma pauta pelo seu identificador")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Pauta encontrada", content = @Content(schema = @Schema(implementation = PautaResponse.class))),
			@ApiResponse(responseCode = "404", description = "Pauta não encontrada") })
	public PautaResponse findById(
			@Parameter(description = "Identificador da pauta", example = "1", required = true) @PathVariable Long id) {
		return pautaService.findById(id);
	}
}