package dev.rodrigo.desafiovotacao.mappers;

import dev.rodrigo.desafiovotacao.dto.SessaoResponseDto;
import dev.rodrigo.desafiovotacao.entity.SessaoVotacao;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SessaoVotacaoMapper {
  @Mapping(source = "pauta.id", target = "pautaId")
  SessaoResponseDto toSessaoResponseDto(SessaoVotacao sessaoVotacao);
}
