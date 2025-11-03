package com.votacao.presentation.controller;


import com.votacao.application.dto.RegistrarVotoRequestDTO;
import com.votacao.application.usecase.RegistrarVotoUseCase;
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
@RequestMapping("/api/v1/votos")
@RequiredArgsConstructor
public class VotoController {

    private final RegistrarVotoUseCase registrarVotoUseCase;

    @Operation(summary = "Registra um voto em uma sessão", description = "Registra um voto de um associado (Sim ou Não) em uma sessão de votação específica.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Voto registrado com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @PostMapping
    public ResponseEntity<Void> registrarVoto(@RequestBody @Valid RegistrarVotoRequestDTO registrarVotoRequestDTO) {
        registrarVotoUseCase.execute(registrarVotoRequestDTO.getIdSessao(), registrarVotoRequestDTO.getEscolha());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
