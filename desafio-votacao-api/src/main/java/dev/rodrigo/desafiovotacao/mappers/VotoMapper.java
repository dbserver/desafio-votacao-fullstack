package dev.rodrigo.desafiovotacao.mappers;

import dev.rodrigo.desafiovotacao.dto.VotoResponseDto;
import dev.rodrigo.desafiovotacao.entity.Voto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VotoMapper {
  @Mapping(source = "sessao.id", target = "sessaoId")
  @Mapping(source = "tipoVoto", target = "voto")
  @Mapping(source = "cpf.numero", target = "cpf")
  VotoResponseDto toVotoResponseDto(Voto voto);
}
