package com.exemplo.projeto.adapter.output.mapper;

import com.exemplo.projeto.adapter.output.entity.SessaoEntity;
import com.exemplo.projeto.adapter.input.dto.SessaoResponseDTO;
import com.exemplo.projeto.domain.model.Sessao;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SessaoMapper {
    SessaoResponseDTO toResponse(Sessao sessao);

    SessaoEntity toEntity(Sessao pauta);

    Sessao toDomain(SessaoEntity entity);
}