package com.votacao.backend.api.v1.controller;

import com.votacao.backend.api.v1.dto.AbrirSessaoRequest;
import com.votacao.backend.api.v1.dto.SessaoResponse;
import com.votacao.backend.domain.entity.Sessao;
import com.votacao.backend.domain.services.SessaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@Tag(name = "Sessões", description = "Abertura de sessões de votação")
@RestController
@RequestMapping("/api/v1/pautas")
@RequiredArgsConstructor
public class SessaoController {

    private final SessaoService sessaoService;

    @Operation(summary = "Abrir sessão de votação para uma pauta")
    @ApiResponse(responseCode = "201", description = "Sessão aberta com sucesso")
    @ApiResponse(responseCode = "404", description = "Pauta não encontrada")
    @ApiResponse(responseCode = "409", description = "Já existe uma sessão aberta para esta pauta")
    @PostMapping("/{pautaId}/sessao")
    public ResponseEntity<SessaoResponse> abrir(
            @PathVariable UUID pautaId,
            @RequestBody(required = false) AbrirSessaoRequest request
    ) {
        Integer duracao = request != null ? request.duracaoMinutos() : null;
        Sessao sessao = sessaoService.abrir(pautaId, duracao);
        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/sessoes/{id}")
                .buildAndExpand(sessao.getId())
                .toUri();

        return ResponseEntity.created(location).body(SessaoResponse.from(sessao));
    }

    @Operation(summary = "Consultar sessão por pauta")
    @ApiResponse(responseCode = "200", description = "Resultado retornado com sucesso")
    @ApiResponse(responseCode = "404", description = "Sessão não encontrada")
    @GetMapping("/{pautaId}/sessao")
    public ResponseEntity<SessaoResponse> buscarPorPauta(@PathVariable UUID pautaId) {
        return ResponseEntity.status(HttpStatus.OK).body(SessaoResponse.from(sessaoService.buscarPorPautaId(pautaId)));
    }
}
