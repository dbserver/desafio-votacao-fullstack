package com.example.votacao.sessao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.example.votacao.pauta.Pauta;
import com.example.votacao.pauta.PautaRepository;
import com.example.votacao.sessao.dto.CriarSessaoRequest;
import com.example.votacao.shared.ConflitoException;
import com.example.votacao.shared.RecursoNaoEncontradoException;

@ExtendWith(MockitoExtension.class)
class SessaoServiceTest {

  private static final Long PAUTA_ID = 4L;
  private static final Duration DURACAO_PADRAO = Duration.ofMinutes(1);

  @Mock
  private SessaoRepository sessaoRepository;

  @Mock
  private PautaRepository pautaRepository;

  @InjectMocks
  private SessaoService sessaoService;

  private Pauta pauta;

  @BeforeEach
  void setUp() {
    ReflectionTestUtils.setField(sessaoService, "duracaoPadrao", DURACAO_PADRAO);

    pauta = new Pauta("Reforma do estatuto", "Aprovar a nova redação");
    ReflectionTestUtils.setField(pauta, "id", PAUTA_ID);
  }

  @Test
  @DisplayName("Deve abrir a sessão com a duração padrão quando o corpo não for enviado")
  void deveAbrirSessaoComDuracaoPadraoSemCorpo() {
    mockPautaSemSessao();

    Sessao sessao = sessaoService.create(PAUTA_ID, null);

    assertThat(Duration.between(sessao.getInicio(), sessao.getFim())).isEqualTo(DURACAO_PADRAO);
  }

  @Test
  @DisplayName("Deve abrir a sessão com a duração padrão quando a duração não for informada")
  void deveAbrirSessaoComDuracaoPadraoSemDuracao() {
    mockPautaSemSessao();

    Sessao sessao = sessaoService.create(PAUTA_ID, new CriarSessaoRequest(null));

    assertThat(Duration.between(sessao.getInicio(), sessao.getFim())).isEqualTo(DURACAO_PADRAO);
  }

  @Test
  @DisplayName("Deve abrir a sessão com a duração informada em minutos")
  void deveAbrirSessaoComDuracaoInformada() {
    mockPautaSemSessao();

    Sessao sessao = sessaoService.create(PAUTA_ID, new CriarSessaoRequest(120));

    ArgumentCaptor<Sessao> captor = ArgumentCaptor.forClass(Sessao.class);
    verify(sessaoRepository).save(captor.capture());
    assertThat(captor.getValue().getPauta()).isSameAs(pauta);
    assertThat(Duration.between(captor.getValue().getInicio(), captor.getValue().getFim()))
        .isEqualTo(Duration.ofHours(2));
    assertThat(sessao).isSameAs(captor.getValue());
  }

  @Test
  @DisplayName("Deve lançar exceção quando a pauta não existir")
  void deveRejeitarPautaInexistente() {
    when(pautaRepository.findById(PAUTA_ID)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> sessaoService.create(PAUTA_ID, null))
        .isInstanceOf(RecursoNaoEncontradoException.class)
        .hasMessage("Pauta não encontrada");

    verify(sessaoRepository, never()).save(any());
  }

  @Test
  @DisplayName("Deve lançar exceção quando a pauta já tiver sessão")
  void deveRejeitarSessaoDuplicada() {
    when(pautaRepository.findById(PAUTA_ID)).thenReturn(Optional.of(pauta));
    when(sessaoRepository.existsByPautaId(PAUTA_ID)).thenReturn(true);

    assertThatThrownBy(() -> sessaoService.create(PAUTA_ID, null))
        .isInstanceOf(ConflitoException.class)
        .hasMessage("Já existe uma sessão para esta pauta");

    verify(sessaoRepository, never()).save(any());
  }

  @Test
  @DisplayName("Deve retornar a sessão da pauta")
  void deveBuscarSessaoPorPauta() {
    Sessao sessao = new Sessao(pauta, DURACAO_PADRAO);
    when(sessaoRepository.findByPautaId(PAUTA_ID)).thenReturn(Optional.of(sessao));

    assertThat(sessaoService.getByPautaId(PAUTA_ID)).isSameAs(sessao);
  }

  @Test
  @DisplayName("Deve lançar exceção quando a pauta não tiver sessão")
  void deveRejeitarBuscaSemSessao() {
    when(sessaoRepository.findByPautaId(PAUTA_ID)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> sessaoService.getByPautaId(PAUTA_ID))
        .isInstanceOf(RecursoNaoEncontradoException.class)
        .hasMessage("Sessão não encontrada para esta pauta");
  }

  private void mockPautaSemSessao() {
    when(pautaRepository.findById(PAUTA_ID)).thenReturn(Optional.of(pauta));
    when(sessaoRepository.existsByPautaId(PAUTA_ID)).thenReturn(false);
    when(sessaoRepository.save(any(Sessao.class))).thenAnswer(invocation -> invocation.getArgument(0));
  }
}
