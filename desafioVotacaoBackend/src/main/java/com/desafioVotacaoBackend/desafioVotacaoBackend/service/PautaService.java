package com.desafioVotacaoBackend.desafioVotacaoBackend.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.desafioVotacaoBackend.desafioVotacaoBackend.model.Pauta;
import com.desafioVotacaoBackend.desafioVotacaoBackend.repository.PautaRepository;

@Service
public class PautaService {

    @Autowired
    private PautaRepository repository;

    public Pauta criarPauta(String titulo, String descricao) {
        Pauta pauta = new Pauta();
        pauta.setTitulo(titulo);
        pauta.setDescricao(descricao);
        pauta.setSessaoAtiva(false);
        return repository.save(pauta);
    }

    public Pauta abrirSessao(Long pautaId, Integer minutos) {
        Optional<Pauta> optionalPauta = repository.findById(pautaId);
        if (optionalPauta.isEmpty()) {
            throw new RuntimeException("Pauta não encontrada");
        }

        Pauta pauta = optionalPauta.get();
        pauta.setDataAbertura(LocalDateTime.now());
        pauta.setDataFechamento(pauta.getDataAbertura().plusMinutes(minutos != null ? minutos : 1));
        pauta.setSessaoAtiva(true);

        return repository.save(pauta);
    }

    public List<Pauta> listarTodasPautas() {
        return repository.findAll();
    }

    public Optional<Pauta> buscarPautaPorId(Long id) {
        return repository.findById(id);
    }

    public boolean verificarSessaoAtiva(Long pautaId) {
        Optional<Pauta> optionalPauta = repository.findById(pautaId);
        if (optionalPauta.isEmpty()) {
            return false;
        }

        Pauta pauta = optionalPauta.get();
        return pauta.getSessaoAtiva() && LocalDateTime.now().isBefore(pauta.getDataFechamento());
    }
}
