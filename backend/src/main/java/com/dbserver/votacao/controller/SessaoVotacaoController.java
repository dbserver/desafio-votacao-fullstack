package com.dbserver.votacao.controller;

import com.dbserver.votacao.dto.SessaoVotacaoRequest;
import com.dbserver.votacao.dto.SessaoVotacaoResponse;
import com.dbserver.votacao.service.SessaoVotacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/sessoes")
@RequiredArgsConstructor
@Tag(name = "Sessões de Votação", description = "Gerenciamento de sessões de votação")
public class SessaoVotacaoController {

    private final SessaoVotacaoService sessaoVotacaoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Abrir sessão de votação em uma pauta")
    public SessaoVotacaoResponse abrirSessao(@Valid @RequestBody SessaoVotacaoRequest request) {
        return sessaoVotacaoService.abrirSessao(request);
    }

    @GetMapping("/pauta/{pautaId}")
    @Operation(summary = "Buscar sessão de votação por pauta")
    public SessaoVotacaoResponse buscarPorPauta(@PathVariable Long pautaId) {
        return sessaoVotacaoService.buscarPorPautaId(pautaId);
    }
}
