package com.dbserver.desafiovotacaofullstack.dtos;

import java.time.LocalDateTime;

public record SessionResponseDto(Integer idAgenda, LocalDateTime startTime, LocalDateTime endTime) {

}
