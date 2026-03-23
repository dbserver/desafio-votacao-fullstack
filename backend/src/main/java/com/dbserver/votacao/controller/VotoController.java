package com.dbserver.votacao.controller;

import com.dbserver.votacao.dto.ResultadoVotacaoResponse;
import com.dbserver.votacao.dto.VotoRequest;
import com.dbserver.votacao.dto.VotoResponse;
import com.dbserver.votacao.service.VotoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/pautas/{pautaId}/votos")
@RequiredArgsConstructor
@Tag(name = "Votos", description = "Votação em pautas")
public class VotoController {

    private final VotoService votoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Registrar voto em uma pauta")
    public VotoResponse votar(@PathVariable Long pautaId, @Valid @RequestBody VotoRequest request) {
        return votoService.votar(pautaId, request);
    }

    @GetMapping("/resultado")
    @Operation(summary = "Contabilizar resultado da votação")
    public ResultadoVotacaoResponse resultado(@PathVariable Long pautaId) {
        return votoService.resultado(pautaId);
    }
}
