package com.nunesd66.pautas_api.sessaoVotacao.dto;

import com.nunesd66.pautas_api.voto.model.Voto;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;

public record SessaoVotacaoResponse(
    Long id,
    LocalDateTime dataAbertura,
    LocalDateTime dataFechamento,
    List<Voto> votos
) {}
