package com.example.votacao.sessao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.votacao.sessao.dto.CriarSessaoRequest;
import com.example.votacao.sessao.dto.SessaoResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Sessao", description = "Cadastro de sessao")
@RestController
@RequestMapping("/api/v1/pauta/{pautaId}/sessao")
public class SessaoController {

  @Autowired
  private SessaoService sessaoService;

  @Operation(summary = "Cria uma nova sessao")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Sessão aberta"),
      @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
      @ApiResponse(responseCode = "404", description = "Pauta não encontrada", content = @Content),
      @ApiResponse(responseCode = "409", description = "Pauta já possui sessão", content = @Content)
  })
  @PostMapping
  public ResponseEntity<SessaoResponse> create(@PathVariable Long pautaId,
      @Valid @RequestBody(required = false) CriarSessaoRequest request) {
    Sessao sessao = sessaoService.create(pautaId, request);

    return ResponseEntity.status(HttpStatus.CREATED).body(SessaoResponse.from(sessao));
  }

  @Operation(summary = "Lista todas as pautas")
  @ApiResponse(responseCode = "200", description = "Lista de pautas")
  @GetMapping
  public ResponseEntity<SessaoResponse> getSessaoByPautaId(@PathVariable Long pautaId) {
    Sessao sessao = sessaoService.getByPautaId(pautaId);

    return ResponseEntity.ok(SessaoResponse.from(sessao));
  }
}
