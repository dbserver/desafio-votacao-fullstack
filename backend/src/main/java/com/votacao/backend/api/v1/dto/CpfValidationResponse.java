package com.votacao.backend.api.v1.dto;

import com.votacao.backend.infrastructure.cpf.CpfStatus;

public record CpfValidationResponse(CpfStatus status) {
    public static CpfValidationResponse from(CpfStatus status) {
        return new CpfValidationResponse(status);
    }
}
