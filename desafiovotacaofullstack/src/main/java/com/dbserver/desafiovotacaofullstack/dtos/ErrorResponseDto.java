package com.dbserver.desafiovotacaofullstack.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ErrorResponseDto {
	
	private String title;
	private Integer status;
	private String detail;
	private String instance;
}
