package com.example.votacao.associado;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.example.votacao.associado.dto.CriarAssociadoRequest;
import com.example.votacao.shared.ConflitoException;
import com.example.votacao.shared.RecursoNaoEncontradoException;

@Service
public class AssociadoService {

  @Autowired
  private AssociadoRepository associadoRepository;

  public Associado create(CriarAssociadoRequest request) {
    Associado associado = new Associado(request.nome(), request.cpf());

    if (associadoRepository.existsByCpf(associado.getCpf())) {
      throw new ConflitoException("Já existe um associado com este CPF");
    }

    try {
      return associadoRepository.saveAndFlush(associado);
    } catch (DataIntegrityViolationException e) {
      throw new ConflitoException("Já existe um associado com este CPF");
    }
  }

  public Associado getByCpf(String cpf) {
    return associadoRepository.findByCpf(Associado.somenteDigitos(cpf))
        .orElseThrow(() -> new RecursoNaoEncontradoException("Associado não encontrado"));
  }

  public List<Associado> getAllAssociados() {
    return associadoRepository.findAll();
  }
}
