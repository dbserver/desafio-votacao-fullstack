package com.dbchal.voting.dto.voto;

import java.time.Instant;

import com.dbchal.voting.domain.entity.Voto;
import com.dbchal.voting.domain.enums.OpcaoVoto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "VotoResponse", description = "Dados retornados após o registro de um voto")
public record VotoResponse(

		@Schema(description = "Identificador único do voto", example = "1")
		Long id,

		@Schema(description = "Identificador da sessão de votação", example = "10")
		Long sessaoId,

		@Schema(description = "Identificador da pauta relacionada à votação", example = "1")
		Long pautaId,

		@Schema(description = "Identificador do associado que realizou o voto", example = "20")
		Long associadoId,

		@Schema(description = "Opção escolhida pelo associado", example = "SIM", allowableValues = { "SIM", "NAO" })
		OpcaoVoto opcao,

		@Schema(description = "Data e hora em que o voto foi registrado", example = "2026-07-25T18:30:00Z")
		Instant dthVoto

) {

	public static VotoResponse from(Voto voto) {
		return new VotoResponse(
				voto.getId(),
				voto.getSessao().getId(),
				voto.getSessao().getPauta().getId(),
				voto.getAssociado().getId(),
				voto.getOpcao(),
				voto.getDthVoto()
		);
	}
}