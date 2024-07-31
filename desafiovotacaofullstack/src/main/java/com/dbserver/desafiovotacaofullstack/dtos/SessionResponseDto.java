package com.dbserver.desafiovotacaofullstack.dtos;

import java.time.LocalDateTime;

public record SessionResponseDto(Integer id, AgendaResponseDto agendaResponseDto, LocalDateTime startTime, LocalDateTime endTime) {

}
