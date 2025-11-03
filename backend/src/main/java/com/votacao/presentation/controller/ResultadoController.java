package com.votacao.presentation.controller;

import com.votacao.application.dto.ResultadoVotacaoResponseDTO;
import com.votacao.application.usecase.ContabilizarVotosUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/resultados")
@RequiredArgsConstructor
public class ResultadoController {

    private final ContabilizarVotosUseCase contabilizarVotosUseCase;

    @Operation(
            summary = "Obter resultado da sessão de votação",
            description = "Retorna votos SIM/NAO, totais e dados da pauta e sessão para a sessão informada."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resultado obtido com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResultadoVotacaoResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)})
    @GetMapping("/{idSessao}")
    public ResponseEntity<ResultadoVotacaoResponseDTO> obterResultado(@PathVariable Long idSessao) {
        var resultado = contabilizarVotosUseCase.execute(idSessao);
        return ResponseEntity.ok(resultado);
    }
}
