package com.dbchal.voting.dto.common;

import java.util.List;

import org.springframework.data.domain.Page;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "PageResponse", description = "Resultado paginado")
public record PageResponse<T>(

		List<T> content,

		@Schema(example = "0")
		int page,

		@Schema(example = "10")
		int size,

		@Schema(example = "42")
		long totalElements,

		@Schema(example = "5")
		int totalPages,

		boolean first,

		boolean last

) {

	public static <T> PageResponse<T> from(Page<T> result) {
		return new PageResponse<>(
				result.getContent(),
				result.getNumber(),
				result.getSize(),
				result.getTotalElements(),
				result.getTotalPages(),
				result.isFirst(),
				result.isLast()
		);
	}
}
