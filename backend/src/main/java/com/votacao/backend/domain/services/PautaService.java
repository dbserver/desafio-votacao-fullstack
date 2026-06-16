package com.votacao.backend.domain.services;

import com.votacao.backend.domain.entity.Pauta;
import com.votacao.backend.domain.repository.PautaRepository;
import com.votacao.backend.infrastructure.exception.PautaNaoEncontradaException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PautaService {

    private final PautaRepository pautaRepository;

    @Transactional
    public Pauta criar(String titulo, String descricao) {
        log.info("Criando pauta: {}", titulo);
        Pauta pauta = new Pauta();
        pauta.setTitulo(titulo);
        pauta.setDescricao(descricao);
        return pautaRepository.save(pauta);
    }

    @Transactional(readOnly = true)
    public List<Pauta> listar() {
        return pautaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Page<Pauta> listar(Pageable pageable) {
        return pautaRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Pauta buscarPorId(UUID id) {
        return pautaRepository.findById(id)
                .orElseThrow(() -> new PautaNaoEncontradaException(id));
    }

}
