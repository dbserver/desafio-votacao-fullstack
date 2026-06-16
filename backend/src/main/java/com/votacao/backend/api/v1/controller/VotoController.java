package com.votacao.backend.api.v1.controller;

import com.votacao.backend.api.v1.dto.RegistrarVotoRequest;
import com.votacao.backend.api.v1.dto.ResultadoResponse;
import com.votacao.backend.api.v1.dto.VotoResponse;
import com.votacao.backend.domain.entity.Voto;
import com.votacao.backend.domain.services.VotoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@Tag(name = "Votos", description = "Registro e resultado de votos")
@RestController
@RequestMapping("/api/v1/sessoes")
@RequiredArgsConstructor
public class VotoController {

    private final VotoService votoService;

    @Operation(summary = "Registrar voto em uma sessão")
    @ApiResponse(responseCode = "201", description = "Voto registrado com sucesso")
    @ApiResponse(responseCode = "404", description = "Sessão não encontrada")
    @ApiResponse(responseCode = "409", description = "Associado já votou nesta sessão")
    @ApiResponse(responseCode = "403", description = "CPF não autorizado para votar")
    @ApiResponse(responseCode = "422", description = "Sessão encerrada")
    @PostMapping("/{sessaoId}/votos")
    public ResponseEntity<VotoResponse> votar(
            @PathVariable UUID sessaoId,
            @RequestBody @Valid RegistrarVotoRequest request
    ) {
        Voto voto = votoService.registrar(sessaoId, request.associadoId(), request.opcao());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{votoId}")
                .buildAndExpand(voto.getId())
                .toUri();

        return ResponseEntity.created(location).body(VotoResponse.from(voto));
    }

    @Operation(summary = "Consultar resultado de uma sessão")
    @ApiResponse(responseCode = "200", description = "Resultado retornado com sucesso")
    @ApiResponse(responseCode = "404", description = "Sessão não encontrada")
    @GetMapping("/{sessaoId}/resultado")
    public ResponseEntity<ResultadoResponse> resultado(@PathVariable UUID sessaoId) {
        return ResponseEntity.status(HttpStatus.OK).body(ResultadoResponse.from(votoService.resultado(sessaoId)));
    }

    @Operation(summary = "Buscar voto por ID dentro de uma sessão")
    @ApiResponse(responseCode = "200", description = "Voto encontrado")
    @ApiResponse(responseCode = "404", description = "Voto não encontrado")
    @GetMapping("/{sessaoId}/votos/{votoId}")
    public ResponseEntity<VotoResponse> buscarVoto(
            @PathVariable UUID sessaoId,
            @PathVariable UUID votoId
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(VotoResponse.from(votoService.buscarPorId(sessaoId, votoId)));
    }
}
