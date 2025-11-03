package com.votacao.presentation.controller;

import com.votacao.application.dto.SessaoVotacaoRequestDTO;
import com.votacao.application.usecase.AbrirSessaoUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/sessao")
@RequiredArgsConstructor
public class SessaoController {

    private final AbrirSessaoUseCase abrirSessaoUseCase;

    @Operation(summary = "Abre uma nova sessão de votação", description = "Abre uma sessão vinculada a uma pauta com duração definida.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Sessão criada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @PostMapping
    public ResponseEntity<Void> abrirSessao(@RequestBody @Valid SessaoVotacaoRequestDTO request) {
        abrirSessaoUseCase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
