package com.example.votacao.voto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.util.ReflectionTestUtils;

import com.example.votacao.associado.Associado;
import com.example.votacao.associado.AssociadoRepository;
import com.example.votacao.cpf.CpfClient;
import com.example.votacao.cpf.StatusCpf;
import com.example.votacao.pauta.Pauta;
import com.example.votacao.sessao.Sessao;
import com.example.votacao.sessao.SessaoService;
import com.example.votacao.shared.ConflitoException;
import com.example.votacao.shared.RecursoNaoEncontradoException;
import com.example.votacao.voto.dto.CriarVotoRequest;

@ExtendWith(MockitoExtension.class)
class VotoServiceTest {

  private static final Long PAUTA_ID = 4L;
  private static final Long SESSAO_ID = 1L;
  private static final Long ASSOCIADO_ID = 3L;
  private static final String CPF = "52998224725";

  @Mock
  private VotoRepository votoRepository;

  @Mock
  private AssociadoRepository associadoRepository;

  @Mock
  private SessaoService sessaoService;

  @Mock
  private CpfClient cpfClient;

  @InjectMocks
  private VotoService votoService;

  private Pauta pauta;
  private Associado associado;

  @BeforeEach
  void setUp() {
    pauta = new Pauta("Reforma do estatuto", "Aprovar a nova redação");
    ReflectionTestUtils.setField(pauta, "id", PAUTA_ID);

    associado = new Associado("Associado Fulano", CPF);
    ReflectionTestUtils.setField(associado, "id", ASSOCIADO_ID);
  }

  @Test
  @DisplayName("Deve registrar o voto do associado na sessão da pauta")
  void deveRegistrarVoto() {
    Sessao sessao = sessaoAberta();
    when(sessaoService.getByPautaId(PAUTA_ID)).thenReturn(sessao);
    when(associadoRepository.findById(ASSOCIADO_ID)).thenReturn(Optional.of(associado));
    when(votoRepository.existsBySessaoIdAndAssociadoId(SESSAO_ID, ASSOCIADO_ID)).thenReturn(false);
    when(cpfClient.verificar(CPF)).thenReturn(StatusCpf.ABLE_TO_VOTE);
    when(votoRepository.saveAndFlush(any(Voto.class))).thenAnswer(invocation -> invocation.getArgument(0));

    Voto voto = votoService.create(PAUTA_ID, new CriarVotoRequest(ASSOCIADO_ID, OpcaoVoto.SIM));

    ArgumentCaptor<Voto> captor = ArgumentCaptor.forClass(Voto.class);
    verify(votoRepository).saveAndFlush(captor.capture());
    assertThat(captor.getValue().getSessao()).isSameAs(sessao);
    assertThat(captor.getValue().getAssociado()).isSameAs(associado);
    assertThat(captor.getValue().getOpcao()).isEqualTo(OpcaoVoto.SIM);
    assertThat(captor.getValue().getCriadoEm()).isNotNull();
    assertThat(voto).isSameAs(captor.getValue());
  }

  @Test
  @DisplayName("Deve lançar exceção quando a pauta não tiver sessão")
  void deveRejeitarPautaSemSessao() {
    when(sessaoService.getByPautaId(PAUTA_ID))
        .thenThrow(new RecursoNaoEncontradoException("Sessão não encontrada para esta pauta"));

    assertThatThrownBy(() -> votoService.create(PAUTA_ID, new CriarVotoRequest(ASSOCIADO_ID, OpcaoVoto.SIM)))
        .isInstanceOf(RecursoNaoEncontradoException.class)
        .hasMessage("Sessão não encontrada para esta pauta");

    verify(votoRepository, never()).saveAndFlush(any());
  }

  @Test
  @DisplayName("Deve lançar exceção quando a sessão estiver encerrada")
  void deveRejeitarSessaoEncerrada() {
    when(sessaoService.getByPautaId(PAUTA_ID)).thenReturn(sessaoEncerrada());

    assertThatThrownBy(() -> votoService.create(PAUTA_ID, new CriarVotoRequest(ASSOCIADO_ID, OpcaoVoto.SIM)))
        .isInstanceOf(ConflitoException.class)
        .hasMessage("Sessão de votação encerrada");

    verify(associadoRepository, never()).findById(anyLong());
    verify(votoRepository, never()).saveAndFlush(any());
  }

  @Test
  @DisplayName("Deve lançar exceção quando o associado não existir")
  void deveRejeitarAssociadoInexistente() {
    when(sessaoService.getByPautaId(PAUTA_ID)).thenReturn(sessaoAberta());
    when(associadoRepository.findById(ASSOCIADO_ID)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> votoService.create(PAUTA_ID, new CriarVotoRequest(ASSOCIADO_ID, OpcaoVoto.SIM)))
        .isInstanceOf(RecursoNaoEncontradoException.class)
        .hasMessage("Associado não encontrado");

    verify(votoRepository, never()).saveAndFlush(any());
  }

  @Test
  @DisplayName("Deve lançar exceção quando o associado já tiver votado")
  void deveRejeitarVotoDuplicado() {
    when(sessaoService.getByPautaId(PAUTA_ID)).thenReturn(sessaoAberta());
    when(associadoRepository.findById(ASSOCIADO_ID)).thenReturn(Optional.of(associado));
    when(votoRepository.existsBySessaoIdAndAssociadoId(SESSAO_ID, ASSOCIADO_ID)).thenReturn(true);

    assertThatThrownBy(() -> votoService.create(PAUTA_ID, new CriarVotoRequest(ASSOCIADO_ID, OpcaoVoto.NAO)))
        .isInstanceOf(ConflitoException.class)
        .hasMessage("Associado já votou nesta pauta");

    verify(cpfClient, never()).verificar(any());
    verify(votoRepository, never()).saveAndFlush(any());
  }

  @Test
  @DisplayName("Deve lançar exceção quando o CPF do associado for inválido")
  void deveRejeitarCpfInvalido() {
    when(sessaoService.getByPautaId(PAUTA_ID)).thenReturn(sessaoAberta());
    when(associadoRepository.findById(ASSOCIADO_ID)).thenReturn(Optional.of(associado));
    when(votoRepository.existsBySessaoIdAndAssociadoId(SESSAO_ID, ASSOCIADO_ID)).thenReturn(false);
    when(cpfClient.verificar(CPF)).thenThrow(new RecursoNaoEncontradoException("CPF inválido"));

    assertThatThrownBy(() -> votoService.create(PAUTA_ID, new CriarVotoRequest(ASSOCIADO_ID, OpcaoVoto.SIM)))
        .isInstanceOf(RecursoNaoEncontradoException.class)
        .hasMessage("CPF inválido");

    verify(votoRepository, never()).saveAndFlush(any());
  }

  @Test
  @DisplayName("Deve lançar exceção quando o associado não estiver apto a votar")
  void deveRejeitarAssociadoInapto() {
    when(sessaoService.getByPautaId(PAUTA_ID)).thenReturn(sessaoAberta());
    when(associadoRepository.findById(ASSOCIADO_ID)).thenReturn(Optional.of(associado));
    when(votoRepository.existsBySessaoIdAndAssociadoId(SESSAO_ID, ASSOCIADO_ID)).thenReturn(false);
    when(cpfClient.verificar(CPF)).thenReturn(StatusCpf.UNABLE_TO_VOTE);

    assertThatThrownBy(() -> votoService.create(PAUTA_ID, new CriarVotoRequest(ASSOCIADO_ID, OpcaoVoto.SIM)))
        .isInstanceOf(RecursoNaoEncontradoException.class)
        .hasMessage("Associado não está apto a votar");

    verify(votoRepository, never()).saveAndFlush(any());
  }

  @Test
  @DisplayName("Deve lançar exceção quando o banco rejeitar voto duplicado por concorrência")
  void deveRejeitarVotoDuplicadoConcorrente() {
    when(sessaoService.getByPautaId(PAUTA_ID)).thenReturn(sessaoAberta());
    when(associadoRepository.findById(ASSOCIADO_ID)).thenReturn(Optional.of(associado));
    when(votoRepository.existsBySessaoIdAndAssociadoId(SESSAO_ID, ASSOCIADO_ID)).thenReturn(false);
    when(cpfClient.verificar(CPF)).thenReturn(StatusCpf.ABLE_TO_VOTE);
    when(votoRepository.saveAndFlush(any(Voto.class)))
        .thenThrow(new DataIntegrityViolationException("uk_voto_sessao_associado"));

    assertThatThrownBy(() -> votoService.create(PAUTA_ID, new CriarVotoRequest(ASSOCIADO_ID, OpcaoVoto.SIM)))
        .isInstanceOf(ConflitoException.class)
        .hasMessage("Associado já votou nesta pauta");
  }

  @Test
  @DisplayName("Deve listar os votos pela sessão da pauta")
  void deveListarVotosDaPauta() {
    Sessao sessao = sessaoAberta();
    when(sessaoService.getByPautaId(PAUTA_ID)).thenReturn(sessao);
    when(votoRepository.findBySessaoId(SESSAO_ID)).thenReturn(List.of(
        new Voto(sessao, associado, OpcaoVoto.SIM),
        new Voto(sessao, associado, OpcaoVoto.NAO)));

    List<Voto> resultado = votoService.getByPautaId(PAUTA_ID);

    assertThat(resultado).extracting(Voto::getOpcao).containsExactly(OpcaoVoto.SIM, OpcaoVoto.NAO);
    verify(votoRepository).findBySessaoId(SESSAO_ID);
  }

  @Test
  @DisplayName("Deve lançar exceção ao listar votos de pauta sem sessão")
  void deveRejeitarListagemSemSessao() {
    when(sessaoService.getByPautaId(PAUTA_ID))
        .thenThrow(new RecursoNaoEncontradoException("Sessão não encontrada para esta pauta"));

    assertThatThrownBy(() -> votoService.getByPautaId(PAUTA_ID))
        .isInstanceOf(RecursoNaoEncontradoException.class);

    verify(votoRepository, never()).findBySessaoId(anyLong());
  }

  private Sessao sessaoAberta() {
    return sessao(Duration.ofMinutes(1));
  }

  private Sessao sessaoEncerrada() {
    return sessao(Duration.ofMinutes(-1));
  }

  private Sessao sessao(Duration duracao) {
    Sessao sessao = new Sessao(pauta, duracao);
    ReflectionTestUtils.setField(sessao, "id", SESSAO_ID);
    return sessao;
  }
}
