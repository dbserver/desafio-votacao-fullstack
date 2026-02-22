package dev.rodrigo.desafiovotacao.mappers;

import dev.rodrigo.desafiovotacao.dto.PautaResponseDto;
import dev.rodrigo.desafiovotacao.entity.Pauta;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PautaMapper {
  PautaResponseDto toPautaResponseDto(Pauta pauta);
  List<PautaResponseDto> toPautaResponseList(List<Pauta> pautas);
}
