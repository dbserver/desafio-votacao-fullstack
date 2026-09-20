package com.example.votacao.sessao.dto;

import jakarta.validation.constraints.Positive;

public record CriarSessaoRequest(
        @Positive(message = "A duração deve ser maior que zero") Integer duracaoEmMinutos) {

}
