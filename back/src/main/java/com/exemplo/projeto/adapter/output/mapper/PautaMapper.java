package com.exemplo.projeto.adapter.output.mapper;

import com.exemplo.projeto.adapter.output.entity.PautaEntity;
import com.exemplo.projeto.adapter.input.dto.PautaResponseDTO;
import com.exemplo.projeto.domain.model.Pauta;
import com.exemplo.projeto.domain.model.Sessao;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public interface PautaMapper {

    @Mappings({
            @Mapping(source = "sessao.inicio", target = "inicioSessao"),
            @Mapping(source = "sessao.fim", target = "fimSessao")
    })
    PautaResponseDTO toDto(Pauta pauta);

    @Mappings({
            @Mapping(source = "sessao.inicio", target = "inicioSessao"),
            @Mapping(source = "sessao.fim", target = "fimSessao")
    })
    PautaEntity toEntity(Pauta pauta);

    @Mappings({
            @Mapping(target = "sessao", expression = "java(toSessao(entity.getInicioSessao(), entity.getFimSessao()))"),
            @Mapping(source = "descricao", target = "descricao")
    })
    Pauta toDomain(PautaEntity entity);

    default Sessao toSessao(LocalDateTime inicio, LocalDateTime fim) {
        Sessao sessao = new Sessao();
        sessao.setInicio(inicio);
        sessao.setFim(fim);
        return sessao;
    }
}
