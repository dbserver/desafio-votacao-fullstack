package com.dbchal.voting.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.dbchal.voting.dto.common.PageResponse;
import com.dbchal.voting.dto.pauta.CreatePautaRequest;
import com.dbchal.voting.dto.pauta.PautaResponse;
import com.dbchal.voting.service.PautaQueryService;
import com.dbchal.voting.service.PautaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@Validated
@RestController
@RequestMapping("/api/v1/pautas")
@Tag(name = "Pautas", description = "Cadastro e consulta de pautas")
public class PautaController {

	private final PautaService pautaService;
	private final PautaQueryService pautaQueryService;

	public PautaController(PautaService pautaService, PautaQueryService pautaQueryService) {
		this.pautaService = pautaService;
		this.pautaQueryService = pautaQueryService;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "Cadastrar pauta", description = "Cadastra uma nova pauta")
	@ApiResponses({
			@ApiResponse(responseCode = "201", description = "Pauta cadastrada com sucesso", content = @Content(schema = @Schema(implementation = PautaResponse.class))),
			@ApiResponse(responseCode = "400", description = "Dados da requisição inválidos") })
	public PautaResponse criar(@RequestBody @Valid CreatePautaRequest request) {
		return pautaService.create(request);
	}

	@GetMapping
	@Operation(summary = "Listar pautas", description = "Retorna as pautas de forma paginada, das mais recentes para as mais antigas")
	public PageResponse<PautaResponse> listar(
			@Parameter(description = "Página, iniciando em zero", example = "0") @RequestParam(defaultValue = "0") @Min(0) int page,
			@Parameter(description = "Quantidade por página, entre 1 e 100", example = "10") @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size) {
		var sort = Sort.by(Sort.Order.desc("id"));
		var pageable = PageRequest.of(page, size, sort);

		return PageResponse.from(pautaQueryService.findAll(pageable));
	}

	@GetMapping("/{id}")
	@Operation(summary = "Consultar pauta", description = "Busca uma pauta pelo seu identificador")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Pauta encontrada", content = @Content(schema = @Schema(implementation = PautaResponse.class))),
			@ApiResponse(responseCode = "404", description = "Pauta não encontrada") })
	public PautaResponse buscarPorId(
			@Parameter(description = "Identificador da pauta", example = "1", required = true) @PathVariable Long id) {
		return pautaService.findById(id);
	}
}
