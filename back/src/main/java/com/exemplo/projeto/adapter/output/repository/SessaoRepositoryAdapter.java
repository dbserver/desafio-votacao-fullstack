package com.exemplo.projeto.adapter.output.repository;

import com.exemplo.projeto.adapter.output.entity.SessaoEntity;
import com.exemplo.projeto.adapter.output.mapper.SessaoMapper;
import com.exemplo.projeto.domain.model.Sessao;
import com.exemplo.projeto.application.port.output.SessaoRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SessaoRepositoryAdapter implements SessaoRepositoryPort {

    private final SpringSessaoRepository repository;
    private final SessaoMapper sessaoMapper;

    @Override
    public Optional<Sessao> buscarPorPauta(Long idPauta) {
        return repository.findByIdPauta(idPauta).map(sessaoMapper::toDomain);
    }

    @Override
    public Sessao save(Sessao sessao) {
        SessaoEntity entity = sessaoMapper.toEntity(sessao);
        SessaoEntity saved = repository.save(entity);
        return sessaoMapper.toDomain(saved);
    }
}
