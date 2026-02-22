package dev.rodrigo.desafiovotacao.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import dev.rodrigo.desafiovotacao.dto.ResultadoVotacaoDto;
import dev.rodrigo.desafiovotacao.dto.VotoRequestDto;
import dev.rodrigo.desafiovotacao.entity.SessaoVotacao;
import dev.rodrigo.desafiovotacao.entity.Voto;
import dev.rodrigo.desafiovotacao.enums.ResultadoVotacao;
import dev.rodrigo.desafiovotacao.enums.TipoVoto;
import dev.rodrigo.desafiovotacao.exceptions.RegraNegocioException;
import dev.rodrigo.desafiovotacao.repository.SessaoVotacaoRepository;
import dev.rodrigo.desafiovotacao.repository.VotoRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class VotoServiceTest {

  @Mock private VotoRepository repository;

  @Mock private SessaoVotacaoRepository sessaoRepository;

  @InjectMocks private VotoService service;

  @Test
  void deveRegistrarVotoComSucesso() {

    Long sessaoId = 1L;

    SessaoVotacao sessao = mock(SessaoVotacao.class);
    when(sessao.isAberta()).thenReturn(true);

    VotoRequestDto request = new VotoRequestDto();
    request.setCpf("430.691.550-65");
    request.setVoto(TipoVoto.SIM);

    when(sessaoRepository.findById(sessaoId)).thenReturn(Optional.of(sessao));
    when(repository.existsBySessaoIdAndCpfNumero(sessaoId, request.getCpf())).thenReturn(false);

    Voto voto = service.votar(sessaoId, request);

    assertNotNull(voto);
    assertEquals(TipoVoto.SIM, voto.getTipoVoto());

    verify(repository).save(any(Voto.class));
  }

  @Test
  void naoDevePermitirVotoDuplicado() {

    Long sessaoId = 1L;

    SessaoVotacao sessao = mock(SessaoVotacao.class);
    when(sessao.isAberta()).thenReturn(true);

    VotoRequestDto request = new VotoRequestDto();
    request.setCpf("430.691.550-65");
    request.setVoto(TipoVoto.SIM);

    when(sessaoRepository.findById(sessaoId)).thenReturn(Optional.of(sessao));
    when(repository.existsBySessaoIdAndCpfNumero(sessaoId, request.getCpf())).thenReturn(true);

    assertThrows(RegraNegocioException.class, () -> service.votar(sessaoId, request));
  }

  @Test
  void naoDevePermitirVotoSeSessaoFechada() {

    Long sessaoId = 1L;

    SessaoVotacao sessao = mock(SessaoVotacao.class);
    when(sessao.isAberta()).thenReturn(false);

    when(sessaoRepository.findById(sessaoId)).thenReturn(Optional.of(sessao));

    VotoRequestDto request = new VotoRequestDto();
    request.setCpf("430.691.550-65");
    request.setVoto(TipoVoto.SIM);

    assertThrows(RegraNegocioException.class, () -> service.votar(sessaoId, request));
  }

  @Test
  void deveRetornarResultadoQuandoSessaoEncerrada() {

    Long sessaoId = 1L;

    SessaoVotacao sessao = mock(SessaoVotacao.class);
    when(sessao.isExpirada()).thenReturn(false);
    when(sessao.isAberta()).thenReturn(false);
    when(sessao.getResultado()).thenReturn(ResultadoVotacao.APROVADO);

    when(sessaoRepository.findById(sessaoId)).thenReturn(Optional.of(sessao));
    when(repository.countBySessaoIdAndTipoVoto(sessaoId, TipoVoto.SIM)).thenReturn(5L);
    when(repository.countBySessaoIdAndTipoVoto(sessaoId, TipoVoto.NAO)).thenReturn(3L);

    ResultadoVotacaoDto dto = service.contabilizar(sessaoId);

    assertEquals(5L, dto.getVotosSim());
    assertEquals(3L, dto.getVotosNao());
    assertEquals(8L, dto.getTotal());
    assertEquals(ResultadoVotacao.APROVADO, dto.getResultado());
  }

  @Test
  void deveLancarExcecaoSeSessaoAindaAberta() {

    Long sessaoId = 1L;

    SessaoVotacao sessao = mock(SessaoVotacao.class);
    when(sessao.isExpirada()).thenReturn(false);
    when(sessao.isAberta()).thenReturn(true);

    when(sessaoRepository.findById(sessaoId)).thenReturn(Optional.of(sessao));
    when(repository.countBySessaoIdAndTipoVoto(any(), any())).thenReturn(0L);

    assertThrows(RegraNegocioException.class, () -> service.contabilizar(sessaoId));
  }

  @Test
  void deveEncerrarSessaoSeEstiverExpirada() {

    Long sessaoId = 1L;

    SessaoVotacao sessao = mock(SessaoVotacao.class);

    when(sessao.isExpirada()).thenReturn(true);
    when(sessao.isAberta()).thenReturn(false);
    when(sessao.getResultado()).thenReturn(ResultadoVotacao.APROVADO);

    when(sessaoRepository.findById(sessaoId)).thenReturn(Optional.of(sessao));
    when(repository.countBySessaoIdAndTipoVoto(sessaoId, TipoVoto.SIM)).thenReturn(2L);
    when(repository.countBySessaoIdAndTipoVoto(sessaoId, TipoVoto.NAO)).thenReturn(1L);

    service.contabilizar(sessaoId);

    verify(sessao).encerrar(any());
  }

  @Test
  void deveLancarExcecaoSeSessaoNaoExistir() {
    Long sessaoId = 1L;

    when(sessaoRepository.findById(sessaoId)).thenReturn(Optional.empty());

    assertThrows(RegraNegocioException.class, () -> service.contabilizar(sessaoId));
  }

  @Test
  void deveRetornarReprovadoNoResultadoDaSessao() {

    Long sessaoId = 1L;

    SessaoVotacao sessao = mock(SessaoVotacao.class);

    when(sessao.isExpirada()).thenReturn(true);
    when(sessao.isAberta()).thenReturn(false);

    when(sessaoRepository.findById(sessaoId)).thenReturn(Optional.of(sessao));

    when(repository.countBySessaoIdAndTipoVoto(sessaoId, TipoVoto.SIM)).thenReturn(3L);

    when(repository.countBySessaoIdAndTipoVoto(sessaoId, TipoVoto.NAO)).thenReturn(5L);

    service.contabilizar(sessaoId);

    verify(sessao).encerrar(ResultadoVotacao.REPROVADO);
  }

  @Test
  void deveRetornarEmpateNoResultadoDaSessao() {

    Long sessaoId = 1L;

    SessaoVotacao sessao = mock(SessaoVotacao.class);

    when(sessao.isExpirada()).thenReturn(true);
    when(sessao.isAberta()).thenReturn(false);

    when(sessaoRepository.findById(sessaoId)).thenReturn(Optional.of(sessao));

    when(repository.countBySessaoIdAndTipoVoto(sessaoId, TipoVoto.SIM)).thenReturn(5L);

    when(repository.countBySessaoIdAndTipoVoto(sessaoId, TipoVoto.NAO)).thenReturn(5L);

    service.contabilizar(sessaoId);

    verify(sessao).encerrar(ResultadoVotacao.EMPATE);
  }
}
