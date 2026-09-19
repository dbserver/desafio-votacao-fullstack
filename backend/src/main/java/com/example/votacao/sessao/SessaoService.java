package com.example.votacao.sessao;

import java.time.Duration;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.votacao.pauta.Pauta;
import com.example.votacao.pauta.PautaRepository;
import com.example.votacao.sessao.dto.CriarSessaoRequest;
import com.example.votacao.shared.ConflitoException;
import com.example.votacao.shared.RecursoNaoEncontradoException;

@Service
public class SessaoService {

  @Autowired
  private SessaoRepository sessaoRepository;

  @Autowired
  private PautaRepository pautaRepository;

  @Value("${votacao.sessao.duracao}")
  private Duration duracaoPadrao;

  public Sessao create(Long pautaId, CriarSessaoRequest request) {

    Pauta pauta = pautaRepository.findById(pautaId)
        .orElseThrow(() -> new RecursoNaoEncontradoException("Pauta não encontrada"));

    if (sessaoRepository.existsByPautaId(pautaId)) {
      throw new ConflitoException("Já existe uma sessão para esta pauta");
    }

    Duration duracao = duracaoPadrao;

    if (request != null && request.duracaoEmMinutos() != null) {
      duracao = Duration.ofMinutes(request.duracaoEmMinutos());
    }

    return sessaoRepository.save(new Sessao(pauta, duracao));
  }

  public Sessao getSessaoById(Long pautaId) {
    return getByPautaId(pautaId);
  }

  public Sessao getByPautaId(Long pautaId) {
    return sessaoRepository.findByPautaId(pautaId)
        .orElseThrow(() -> new RecursoNaoEncontradoException("Sessão não encontrada para esta pauta"));
  }
}
