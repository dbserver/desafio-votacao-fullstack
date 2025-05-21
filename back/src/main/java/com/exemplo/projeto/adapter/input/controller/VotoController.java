package com.exemplo.projeto.adapter.input.controller;

import com.exemplo.projeto.adapter.input.dto.ResultadoVotacaoDTO;
import com.exemplo.projeto.adapter.input.dto.VotoRequest;
import com.exemplo.projeto.application.port.input.VotoUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/votos")
@RequiredArgsConstructor
@Tag(name = "Votos", description = "Registro de votos e resultado de pautas")
public class VotoController {
    private final VotoUseCase votoUseCase;

    @Operation(summary = "Registrar voto em uma pauta")
    @ApiResponse(responseCode = "200", description = "Voto registrado com sucesso")
    @PostMapping
    public ResponseEntity<Void> votar(@RequestBody VotoRequest request) {
        votoUseCase.votar(request.getIdPauta(), request.getAssociadoId(), request.isVoto());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Consultar resultado da votação de uma pauta")
    @ApiResponse(responseCode = "200", description = "Resultado da votação retornado com sucesso")
    @GetMapping("/resultado/{idPauta}")
    public ResponseEntity<ResultadoVotacaoDTO> resultado(@PathVariable Long idPauta) {
        return ResponseEntity.ok(votoUseCase.consultarResultado(idPauta));
    }

    @Operation(summary = "Consultar resultado da votação de todas as pautas")
    @ApiResponse(responseCode = "200", description = "Resultado das votações retornado com sucesso")
    @GetMapping("/resultado")
    public ResponseEntity<List<ResultadoVotacaoDTO>> listaResultado() {
        return ResponseEntity.ok(votoUseCase.consultarListaResultado());
    }
}
