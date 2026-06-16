package com.votacao.backend.api.v1.controller;

import com.votacao.backend.api.v1.dto.CpfValidationResponse;
import com.votacao.backend.domain.services.CpfValidationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "CPF", description = "Validação fake de elegibilidade para votação")
@RestController
@RequestMapping("/api/v1/cpfs")
@RequiredArgsConstructor
public class CpfController {

    private final CpfValidationService cpfValidationService;

    @Operation(summary = "Consultar status fake de um CPF")
    @ApiResponse(responseCode = "200", description = "Status retornado com sucesso")
    @ApiResponse(responseCode = "404", description = "CPF não encontrado")
    @GetMapping("/{cpf}")
    public ResponseEntity<CpfValidationResponse> validar(@PathVariable String cpf) {
        return ResponseEntity.status(HttpStatus.OK).body(CpfValidationResponse.from(cpfValidationService.consultarStatus(cpf)));
    }
}
