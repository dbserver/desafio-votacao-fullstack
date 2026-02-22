package dev.rodrigo.desafiovotacao.controller;

import dev.rodrigo.desafiovotacao.dto.SessaoRequestDto;
import dev.rodrigo.desafiovotacao.dto.SessaoResponseDto;
import dev.rodrigo.desafiovotacao.mappers.SessaoVotacaoMapper;
import dev.rodrigo.desafiovotacao.service.SessaoVotacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/sessoes")
@Tag(name = "Sessão de Votação", description = "Operações relacionadas às Sessões de Votação")
public class SessaoVotacaoController {

  private final SessaoVotacaoService service;
  private final SessaoVotacaoMapper mapper;

  @Operation(summary = "Criar uma nova Sessão")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "201", description = "Sessão criada com sucesso"),
        @ApiResponse(
            responseCode = "400",
            description = "Já existe uma sessão aberta para essa pauta"),
        @ApiResponse(
            responseCode = "409",
            description = "Já existe uma sessão aberta para essa pauta")
      })
  @PostMapping("/pauta/{pautaId}")
  public ResponseEntity<SessaoResponseDto> abrirSessao(
      @PathVariable Long pautaId, @RequestBody(required = false) SessaoRequestDto request) {

    if (request == null) {
      request = new SessaoRequestDto();
    }

    SessaoResponseDto response = mapper.toSessaoResponseDto(service.abrirSessao(pautaId, request));

    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @Operation(summary = "Consultar todas sessões")
  @GetMapping("/{id}")
  public SessaoResponseDto buscarSessao(@PathVariable Long id) {
    return mapper.toSessaoResponseDto(service.buscarPorId(id));
  }

  @Operation(summary = "Encerrar uma Sessão manualmente por ID")
  @PostMapping("sessao/encerrar/{sessaoId}")
  public void encerrarSessao(@PathVariable Long sessaoId) {
    service.encerrarSessao(sessaoId);
  }
}
