package com.desafioVotacaoBackend.desafioVotacaoBackend.service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.desafioVotacaoBackend.desafioVotacaoBackend.model.Associado;
import com.desafioVotacaoBackend.desafioVotacaoBackend.repository.AssociadoRepository;

@Service
public class AssociadoService {
    @Autowired
    private AssociadoRepository repository;

    public Associado cadastrarAssociado(String nome, String cpf) {
        Associado associado = new Associado();
        associado.setNome(nome);
        associado.setCpf(cpf);
        return repository.save(associado);
    }

    public Optional<Associado> buscarAssociadoPorId(Long id) {
        return repository.findById(id);
    }
}
