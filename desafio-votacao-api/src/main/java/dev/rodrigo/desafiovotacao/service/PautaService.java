package dev.rodrigo.desafiovotacao.service;

import dev.rodrigo.desafiovotacao.dto.PautaRequestDto;
import dev.rodrigo.desafiovotacao.entity.Pauta;
import dev.rodrigo.desafiovotacao.exceptions.RecursoNaoEncontradoException;
import dev.rodrigo.desafiovotacao.repository.PautaRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PautaService {

  private final PautaRepository repository;

  public void criarNovaPauta(PautaRequestDto dto) {
    Pauta pauta = new Pauta();
    pauta.setTitulo(dto.getTitulo());
    pauta.setDescricao(dto.getDescricao());
    repository.save(pauta);
  }

  public Pauta buscarPautaPorId(Long id) {
    return repository
        .findById(id)
        .orElseThrow(() -> new RecursoNaoEncontradoException("Pauta ID " + id + " não encontrada"));
  }

  public List<Pauta> buscarPautas() {
    return repository.findAll();
  }
}
