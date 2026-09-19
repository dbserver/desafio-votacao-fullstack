package com.example.votacao.associado;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.votacao.associado.dto.CriarAssociadoRequest;

@Service
public class AssociadoService {

  @Autowired
  private AssociadoRepository associadoRepository;

  public Associado create(CriarAssociadoRequest request) {
    Associado associado = new Associado(request.nome());

    associadoRepository.save(associado);

    return associado;
  }

  public List<Associado> getAllAssociados() {
    return associadoRepository.findAll();
  }
}
