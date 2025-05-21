package com.exemplo.projeto.adapter.input.dto;

import java.time.LocalDateTime;

public record PautaResponseDTO(Long id, String titulo, String descricao, LocalDateTime inicioSessao, LocalDateTime fimSessao) {}
