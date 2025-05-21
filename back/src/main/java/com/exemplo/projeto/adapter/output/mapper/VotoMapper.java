package com.exemplo.projeto.adapter.output.mapper;

import com.exemplo.projeto.adapter.output.entity.VotoEntity;
import com.exemplo.projeto.domain.model.Voto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VotoMapper {

    VotoEntity toEntity(Voto voto);

    Voto toDomain(VotoEntity entity);

}
