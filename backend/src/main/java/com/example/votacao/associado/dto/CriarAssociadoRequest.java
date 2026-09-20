package com.example.votacao.associado.dto;

import org.hibernate.validator.constraints.br.CPF;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CriarAssociadoRequest(
    @Schema(description = "Nome do associado", example = "Associado Fulano") @NotBlank(message = "Nome é obrigatório") @Size(max = 255, message = "Nome deve ter no máximo 255 caracteres") String nome,
    @Schema(description = "CPF do associado, com ou sem pontuação", example = "529.982.247-25") @NotBlank(message = "CPF é obrigatório") @CPF(message = "CPF inválido") String cpf) {
}
