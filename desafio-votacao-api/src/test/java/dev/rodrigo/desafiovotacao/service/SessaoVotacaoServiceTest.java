package dev.rodrigo.desafiovotacao.service;

import dev.rodrigo.desafiovotacao.dto.SessaoRequestDto;
import dev.rodrigo.desafiovotacao.entity.Pauta;
import dev.rodrigo.desafiovotacao.entity.SessaoVotacao;
import dev.rodrigo.desafiovotacao.enums.StatusSessao;
import dev.rodrigo.desafiovotacao.exceptions.RecursoNaoEncontradoException;
import dev.rodrigo.desafiovotacao.exceptions.RegraNegocioException;
import dev.rodrigo.desafiovotacao.repository.PautaRepository;
import dev.rodrigo.desafiovotacao.repository.SessaoVotacaoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SessaoVotacaoServiceTest {

  @Mock private SessaoVotacaoRepository repository;

  @Mock private PautaRepository pautaRepository;

  @InjectMocks private SessaoVotacaoService service;

  @Test
  void deveAbrirSessaoComDuracaoInformada() {

    Long pautaId = 1L;

    Pauta pauta = new Pauta();
    pauta.setId(pautaId);

    SessaoRequestDto request = new SessaoRequestDto();
    request.setDuracaoMinutos(10L);

    when(pautaRepository.findById(pautaId)).thenReturn(Optional.of(pauta));
    when(repository.existsByPautaIdAndDataFechamentoAfter(eq(pautaId), any())).thenReturn(false);

    SessaoVotacao sessao = service.abrirSessao(pautaId, request);

    assertNotNull(sessao);
    assertEquals(StatusSessao.ABERTA, sessao.getStatus());
    assertEquals(pauta, sessao.getPauta());

    verify(repository).save(any(SessaoVotacao.class));
  }

  @Test
  void deveAbrirSessaoComDuracaoPadraoQuandoNaoInformada() {

    Long pautaId = 1L;

    Pauta pauta = new Pauta();
    pauta.setId(pautaId);

    SessaoRequestDto request = new SessaoRequestDto();
    request.setDuracaoMinutos(null);

    when(pautaRepository.findById(pautaId)).thenReturn(Optional.of(pauta));
    when(repository.existsByPautaIdAndDataFechamentoAfter(eq(pautaId), any())).thenReturn(false);

    SessaoVotacao sessao = service.abrirSessao(pautaId, request);

    assertNotNull(sessao);
    assertEquals(StatusSessao.ABERTA, sessao.getStatus());

    verify(repository).save(any(SessaoVotacao.class));
  }

  @Test
  void naoDeveAbrirSessaoSeJaExistirSessaoAberta() {

    Long pautaId = 1L;

    Pauta pauta = new Pauta();
    pauta.setId(pautaId);

    when(pautaRepository.findById(pautaId)).thenReturn(Optional.of(pauta));
    when(repository.existsByPautaIdAndDataFechamentoAfter(eq(pautaId), any())).thenReturn(true);

    assertThrows(
        RegraNegocioException.class, () -> service.abrirSessao(pautaId, new SessaoRequestDto()));
  }

  @Test
  void deveLancarExcecaoSePautaNaoExistir() {

    when(pautaRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(
        RecursoNaoEncontradoException.class, () -> service.abrirSessao(1L, new SessaoRequestDto()));
  }

  @Test
  void deveBuscarSessaoPorId() {

    SessaoVotacao sessao = new SessaoVotacao();
    sessao.setId(1L);

    when(repository.findById(1L)).thenReturn(Optional.of(sessao));

    SessaoVotacao resultado = service.buscarPorId(1L);

    assertEquals(1L, resultado.getId());
  }

  @Test
  void deveEncerrarSessaoComSucesso() {

    SessaoVotacao sessao = new SessaoVotacao();
    sessao.setId(1L);
    sessao.setStatus(StatusSessao.ABERTA);

    when(repository.findById(1L)).thenReturn(Optional.of(sessao));

    service.encerrarSessao(1L);

    assertEquals(StatusSessao.ENCERRADA, sessao.getStatus());
    verify(repository).save(sessao);
  }

  @Test
  void naoDeveEncerrarSessaoSeJaEstiverEncerrada() {

    SessaoVotacao sessao = new SessaoVotacao();
    sessao.setId(1L);
    sessao.setStatus(StatusSessao.ENCERRADA);

    when(repository.findById(1L)).thenReturn(Optional.of(sessao));

    assertThrows(RegraNegocioException.class, () -> service.encerrarSessao(1L));
  }

  @Test
  void deveLancarExcecaoSeSessaoNaoExistir() {
    Long sessaoId = 1L;

    when(repository.findById(sessaoId)).thenReturn(Optional.empty());

    assertThrows(RecursoNaoEncontradoException.class, () -> service.buscarPorId(sessaoId));
  }
}
