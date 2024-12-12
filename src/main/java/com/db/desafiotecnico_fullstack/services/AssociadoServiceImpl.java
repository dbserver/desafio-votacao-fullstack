package com.db.desafiotecnico_fullstack.services;

import com.db.desafiotecnico_fullstack.exceptions.ResourceNotFoundException;
import com.db.desafiotecnico_fullstack.models.Associado;
import com.db.desafiotecnico_fullstack.models.dto.AssociadoDTO;
import com.db.desafiotecnico_fullstack.repositories.AssociadoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class AssociadoServiceImpl {

    @Autowired
    private AssociadoRepository associadoRepository;

    public Associado criar(AssociadoDTO associadoDTO) {
        Associado associado = new Associado();
        associado.setNome(associadoDTO.getNome());
        associado.setCpf(associadoDTO.getCpf());
        return associadoRepository.save(associado);
    }

    public List<Associado> listarTodos() {
        return associadoRepository.findAll();
    }

    public Associado buscarPorId(UUID id) {
        return associadoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Associado não encontrado"));
    }

    public void deletar(UUID id) {
        Associado associado = buscarPorId(id);
        associadoRepository.delete(associado);
    }

    public Associado atualizar(UUID id, AssociadoDTO associadoDTO) {
        Associado associado = buscarPorId(id);
        associado.setNome(associadoDTO.getNome());
        associado.setCpf(associadoDTO.getCpf());
        return associadoRepository.save(associado);
    }
}