package com.votacao.presentation.controller;

import com.votacao.application.dto.PautaRequestDTO;
import com.votacao.application.usecase.CriarPautaUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/pauta")
@RequiredArgsConstructor
public class PautaController {

    private final CriarPautaUseCase criarPautaUseCase;

    @Operation(summary = "Cria uma nova pauta", description = "Cria uma nova pauta de votação.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pauta criada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @PostMapping
    public ResponseEntity<Void> criar(@RequestBody @Valid PautaRequestDTO pautaRequestDTO) {
        criarPautaUseCase.execute(pautaRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
