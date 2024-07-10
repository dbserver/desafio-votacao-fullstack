package com.dbserver.desafiovotacaofullstack.dtos;

import java.time.LocalDateTime;

public record SessionRequestDto(Integer idAgenda, LocalDateTime startTime, LocalDateTime endTime) {

}
