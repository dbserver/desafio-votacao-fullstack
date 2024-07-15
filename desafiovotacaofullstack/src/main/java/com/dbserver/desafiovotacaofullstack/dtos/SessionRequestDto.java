package com.dbserver.desafiovotacaofullstack.dtos;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;

public record SessionRequestDto(
		@NotNull(message = "Você deve informar a pauta.")
		Integer idAgenda, 
		@NotNull(message = "Você deve informar o tempo inicial.")
		LocalDateTime startTime, 
		LocalDateTime endTime) {

}
