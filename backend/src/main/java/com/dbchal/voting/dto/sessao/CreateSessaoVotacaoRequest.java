package com.dbchal.voting.dto.sessao;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;

@Schema(name = "CreateSessaoVotacaoRequest", description = "Dados necessários para abrir uma sessão de votação")
public record CreateSessaoVotacaoRequest(

		@Schema(description = "Duração da sessão em minutos. Quando não informada, será utilizada a duração padrão de 1 minuto", example = "5", minimum = "1")
		@Positive(message = "A duração da sessão deve ser maior que 0")
		Long duracao

) {
}