package dev.rodrigo.desafiovotacao.controller;

import dev.rodrigo.desafiovotacao.dto.ResultadoVotacaoDto;
import dev.rodrigo.desafiovotacao.dto.VotoRequestDto;
import dev.rodrigo.desafiovotacao.dto.VotoResponseDto;
import dev.rodrigo.desafiovotacao.mappers.VotoMapper;
import dev.rodrigo.desafiovotacao.service.VotoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/votos")
@RequiredArgsConstructor
@Validated
@Tag(name = "Votos", description = "Operações relacionadas aos votos das sessões")
public class VotoController {

  private final VotoService service;
  private final VotoMapper mapper;

  @Operation(summary = "Registrar voto em uma sessão")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "201", description = "Voto registrado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Erro de validação"),
        @ApiResponse(responseCode = "404", description = "Sessão não encontrada")
      })
  @PostMapping("/sessao/{sessaoId}")
  public ResponseEntity<VotoResponseDto> votar(
      @PathVariable Long sessaoId, @RequestBody VotoRequestDto request) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(mapper.toVotoResponseDto(service.votar(sessaoId, request)));
  }

  @Operation(summary = "Consultar resultado da votação")
  @GetMapping("/sessao/{sessaoId}/resultado")
  public ResultadoVotacaoDto resultado(@PathVariable Long sessaoId) {
    return service.contabilizar(sessaoId);
  }
}
