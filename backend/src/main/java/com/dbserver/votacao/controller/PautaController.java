package com.dbserver.votacao.controller;

import com.dbserver.votacao.dto.PautaRequest;
import com.dbserver.votacao.dto.PautaResponse;
import com.dbserver.votacao.service.PautaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pautas")
@RequiredArgsConstructor
@Tag(name = "Pautas", description = "Gerenciamento de pautas")
public class PautaController {

    private final PautaService pautaService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cadastrar nova pauta")
    public PautaResponse criar(@Valid @RequestBody PautaRequest request) {
        return pautaService.criar(request);
    }

    @GetMapping
    @Operation(summary = "Listar todas as pautas")
    public List<PautaResponse> listarTodas() {
        return pautaService.listarTodas();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar pauta por ID")
    public PautaResponse buscarPorId(@PathVariable Long id) {
        return pautaService.buscarPorId(id);
    }
}
