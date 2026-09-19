package com.example.votacao.associado.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CriarAssociadoRequest(
    @NotBlank(message = "Nome é obrigatório") @Size(max = 255, message = "Nome deve ter no máximo 255 caracteres") String nome) {
}