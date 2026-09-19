package com.example.votacao.pauta;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.votacao.pauta.dto.CriarPautaRequest;

@Service
public class PautaService {

  @Autowired
  private PautaRepository pautaRepository;

  public Pauta create(CriarPautaRequest request) {
    Pauta pauta = new Pauta(request.titulo(), request.descricao());

    pautaRepository.save(pauta);

    return pauta;
  }

  public List<Pauta> getAllPautas() {
    return pautaRepository.findAll();
  }

}
