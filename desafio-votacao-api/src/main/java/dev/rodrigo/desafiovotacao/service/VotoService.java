package dev.rodrigo.desafiovotacao.service;

import dev.rodrigo.desafiovotacao.dto.CpfValidationResponse;
import dev.rodrigo.desafiovotacao.dto.ResultadoVotacaoDto;
import dev.rodrigo.desafiovotacao.dto.VotoRequestDto;
import dev.rodrigo.desafiovotacao.entity.Cpf;
import dev.rodrigo.desafiovotacao.entity.SessaoVotacao;
import dev.rodrigo.desafiovotacao.entity.Voto;
import dev.rodrigo.desafiovotacao.enums.ResultadoVotacao;
import dev.rodrigo.desafiovotacao.enums.TipoVoto;
import dev.rodrigo.desafiovotacao.exceptions.RegraNegocioException;
import dev.rodrigo.desafiovotacao.facade.CpfValidationClient;
import dev.rodrigo.desafiovotacao.repository.SessaoVotacaoRepository;
import dev.rodrigo.desafiovotacao.repository.VotoRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VotoService {

  private final VotoRepository repository;
  private final SessaoVotacaoRepository sessaoRepository;
  private final CpfValidationClient cpfValidationClient;

  @Transactional
  public Voto votar(Long sessaoId, VotoRequestDto request) {

    SessaoVotacao sessao = verificarSessaoAberta(sessaoId);

    CpfValidationResponse response = cpfValidationClient.validarCpf(request.getCpf());

    if ("UNABLE_TO_VOTE".equals(response.status())) {
      throw new RegraNegocioException("Usuário não está apto a votar");
    }

    Voto voto = new Voto();
    voto.setSessao(sessao);
    voto.setCpf(new Cpf(request.getCpf()));
    voto.setTipoVoto(request.getVoto());
    voto.setDataHora(LocalDateTime.now());

    try {
      return repository.save(voto);
    } catch (DataIntegrityViolationException ex) {
      throw new RegraNegocioException("Associado já votou nesta sessão");
    }
  }

  private SessaoVotacao verificarSessaoAberta(Long sessaoId) {
    SessaoVotacao sessao = verificarSeSessaoExiste(sessaoId);

    if (!sessao.isAberta()) {
      throw new RegraNegocioException("Sessão está fechada");
    }
    return sessao;
  }

  @Transactional
  public ResultadoVotacaoDto contabilizar(Long sessaoId) {

    SessaoVotacao sessao = verificarSeSessaoExiste(sessaoId);

    long votosSim = repository.countBySessaoIdAndTipoVoto(sessaoId, TipoVoto.SIM);
    long votosNao = repository.countBySessaoIdAndTipoVoto(sessaoId, TipoVoto.NAO);

    if (sessao.isExpirada()) {

      ResultadoVotacao resultado = calcularResultado(votosSim, votosNao);

      sessao.encerrar(resultado);
    }

    if (sessao.isAberta()) {
      throw new RegraNegocioException("Sessão ainda está aberta");
    }

    return ResultadoVotacaoDto.builder()
        .sessaoId(sessaoId)
        .votosSim(votosSim)
        .votosNao(votosNao)
        .total(votosSim + votosNao)
        .resultado(sessao.getResultado())
        .build();
  }

  private ResultadoVotacao calcularResultado(long votosSim, long votosNao) {

    if (votosSim > votosNao) {
      return ResultadoVotacao.APROVADO;
    }

    if (votosNao > votosSim) {
      return ResultadoVotacao.REPROVADO;
    }

    return ResultadoVotacao.EMPATE;
  }

  private SessaoVotacao verificarSeSessaoExiste(Long sessaoId) {
    return sessaoRepository
        .findById(sessaoId)
        .orElseThrow(() -> new RegraNegocioException("Sessão não encontrada"));
  }
}
