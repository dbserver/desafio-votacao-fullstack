package com.votacao.backend.api.v1.controller;

import com.votacao.backend.api.v1.dto.SessaoResponse;
import com.votacao.backend.domain.entity.Sessao;
import com.votacao.backend.domain.services.SessaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Tag(name = "Sessões", description = "Consulta de sessões de votação")
@RestController
@RequestMapping("/api/v1/sessoes")
@RequiredArgsConstructor
public class SessaoRecursoController {

    private final SessaoService sessaoService;

    @Operation(summary = "Buscar sessão por ID")
    @ApiResponse(responseCode = "200", description = "Sessão encontrada")
    @ApiResponse(responseCode = "404", description = "Sessão não encontrada")
    @GetMapping("/{id}")
    public ResponseEntity<SessaoResponse> buscarPorId(@PathVariable UUID id) {
        Sessao sessao = sessaoService.buscarPorId(id);
        return ResponseEntity.status(HttpStatus.OK).body(SessaoResponse.from(sessao));
    }
}
