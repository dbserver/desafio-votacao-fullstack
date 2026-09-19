package com.example.votacao.voto;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.example.votacao.associado.Associado;
import com.example.votacao.associado.AssociadoRepository;
import com.example.votacao.sessao.Sessao;
import com.example.votacao.sessao.SessaoService;
import com.example.votacao.shared.ConflitoException;
import com.example.votacao.shared.RecursoNaoEncontradoException;
import com.example.votacao.voto.dto.CriarVotoRequest;

@Service
public class VotoService {

  @Autowired
  private VotoRepository votoRepository;

  @Autowired
  private AssociadoRepository associadoRepository;

  @Autowired
  private SessaoService sessaoService;

  public Voto create(Long pautaId, CriarVotoRequest request) {

    Sessao sessao = sessaoService.getByPautaId(pautaId);

    if (!sessao.isAberta()) {
      throw new ConflitoException("Sessão de votação encerrada");
    }

    Associado associado = associadoRepository.findById(request.associadoId())
        .orElseThrow(() -> new RecursoNaoEncontradoException("Associado não encontrado"));

    if (votoRepository.existsBySessaoIdAndAssociadoId(sessao.getId(), associado.getId())) {
      throw new ConflitoException("Associado já votou nesta pauta");
    }

    try {
      return votoRepository.saveAndFlush(new Voto(sessao, associado, request.opcao()));
    } catch (DataIntegrityViolationException e) {
      throw new ConflitoException("Associado já votou nesta pauta");
    }

  }

  public List<Voto> getByPautaId(Long pautaId) {
    Sessao sessao = sessaoService.getByPautaId(pautaId);

    return votoRepository.findBySessaoId(sessao.getId());
  }
}
