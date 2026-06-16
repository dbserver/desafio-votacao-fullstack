package com.votacao.backend.api.v1.dto;

import com.votacao.backend.domain.entity.Pauta;
import org.springframework.data.domain.Page;

import java.util.List;

public record PautaPageResponse(
        List<PautaResponse> content,
        int number,
        int size,
        long totalElements,
        int totalPages,
        boolean first,
        boolean last
) {
    public static PautaPageResponse from(Page<Pauta> page) {
        return new PautaPageResponse(
                page.getContent().stream().map(PautaResponse::from).toList(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isFirst(),
                page.isLast()
        );
    }
}
