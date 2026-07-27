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

import com.dbchal.voting.dto.associado.AssociadoResponse;
import com.dbchal.voting.dto.associado.CreateAssociadoRequest;
import com.dbchal.voting.dto.common.PageResponse;
import com.dbchal.voting.service.AssociadoQueryService;
import com.dbchal.voting.service.AssociadoService;

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
@RequestMapping("/api/v1/associados")
@Tag(name = "Associados", description = "Cadastro e consulta de associados")
public class AssociadoController {

	private final AssociadoService associadoService;
	private final AssociadoQueryService associadoQueryService;

	public AssociadoController(AssociadoService associadoService, AssociadoQueryService associadoQueryService) {
		this.associadoService = associadoService;
		this.associadoQueryService = associadoQueryService;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "Cadastrar associado", description = "Cadastra um novo associado utilizando seu CPF")
	@ApiResponses({
			@ApiResponse(responseCode = "201", description = "Associado cadastrado com sucesso", content = @Content(schema = @Schema(implementation = AssociadoResponse.class))),
			@ApiResponse(responseCode = "400", description = "Dados da requisição inválidos"),
			@ApiResponse(responseCode = "409", description = "Já existe um associado cadastrado com o CPF informado") })
	public AssociadoResponse criar(@RequestBody @Valid CreateAssociadoRequest request) {
		return associadoService.create(request);
	}

	@GetMapping
	@Operation(summary = "Listar associados", description = "Retorna os associados de forma paginada, dos mais recentes para os mais antigos")
	public PageResponse<AssociadoResponse> listar(
			@Parameter(description = "Página, iniciando em zero", example = "0") @RequestParam(defaultValue = "0") @Min(0) int page,
			@Parameter(description = "Quantidade por página, entre 1 e 100", example = "10") @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size) {
		var sort = Sort.by(Sort.Order.desc("id"));
		var pageable = PageRequest.of(page, size, sort);

		return PageResponse.from(associadoQueryService.findAll(pageable));
	}

	@GetMapping("/{id}")
	@Operation(summary = "Consultar associado", description = "Busca um associado pelo seu identificador")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Associado encontrado", content = @Content(schema = @Schema(implementation = AssociadoResponse.class))),
			@ApiResponse(responseCode = "404", description = "Associado não encontrado") })
	public AssociadoResponse buscarPorId(
			@Parameter(description = "Identificador do associado", example = "1", required = true) @PathVariable Long id) {
		return associadoService.findById(id);
	}
}
