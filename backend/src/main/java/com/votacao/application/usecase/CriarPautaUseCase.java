package com.votacao.application.usecase;

import com.votacao.application.dto.PautaRequestDTO;
import com.votacao.domain.model.Pauta;
import com.votacao.domain.service.PautaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CriarPautaUseCase {

    private final PautaService pautaService;

    public void execute(PautaRequestDTO pautaRequestDTO) {
        pautaService.save(Pauta.builder()
                .titulo(pautaRequestDTO.getTitulo())
                .descricao(pautaRequestDTO.getDescricao())
                .build());
    }

}
