package com.exemplo.projeto.adapter.input.controller;

import com.exemplo.projeto.adapter.output.mapper.SessaoMapper;
import com.exemplo.projeto.adapter.input.dto.SessaoRequest;
import com.exemplo.projeto.adapter.input.dto.SessaoResponseDTO;
import com.exemplo.projeto.domain.model.Sessao;
import com.exemplo.projeto.application.port.input.SessaoUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sessoes")
@RequiredArgsConstructor
@Tag(name = "Sessões", description = "Abertura de sessões de votação")
public class SessaoController {

    private final SessaoUseCase sessaoUseCase;
    private final SessaoMapper sessaoMapper;

    @Operation(summary = "Abrir uma nova sessão de votação")
    @ApiResponse(responseCode = "200", description = "Sessão aberta com sucesso")
    @PostMapping
    public ResponseEntity<SessaoResponseDTO> abrirSessao(@RequestBody SessaoRequest request) {
        Sessao sessao = sessaoUseCase.abrirSessao(request.getIdPauta(), request.getMinutos());
        return ResponseEntity.ok(sessaoMapper.toResponse(sessao));
    }
}
