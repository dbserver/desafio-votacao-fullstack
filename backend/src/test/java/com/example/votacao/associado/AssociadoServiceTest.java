package com.example.votacao.associado;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import com.example.votacao.associado.dto.CriarAssociadoRequest;
import com.example.votacao.shared.ConflitoException;
import com.example.votacao.shared.RecursoNaoEncontradoException;

@ExtendWith(MockitoExtension.class)
class AssociadoServiceTest {

  private static final String CPF = "52998224725";

  @Mock
  private AssociadoRepository associadoRepository;

  @InjectMocks
  private AssociadoService associadoService;

  @Test
  @DisplayName("Deve salvar o associado com o nome e o CPF somente com dígitos")
  void deveCriarAssociado() {
    when(associadoRepository.existsByCpf(CPF)).thenReturn(false);
    when(associadoRepository.saveAndFlush(any(Associado.class))).thenAnswer(invocation -> invocation.getArgument(0));

    Associado associado = associadoService.create(new CriarAssociadoRequest("Maria", "529.982.247-25"));

    ArgumentCaptor<Associado> captor = ArgumentCaptor.forClass(Associado.class);
    verify(associadoRepository).saveAndFlush(captor.capture());
    assertThat(captor.getValue().getNome()).isEqualTo("Maria");
    assertThat(captor.getValue().getCpf()).isEqualTo(CPF);
    assertThat(associado).isSameAs(captor.getValue());
  }

  @Test
  @DisplayName("Deve lançar exceção quando o CPF já estiver cadastrado")
  void deveRejeitarCpfDuplicado() {
    when(associadoRepository.existsByCpf(CPF)).thenReturn(true);

    assertThatThrownBy(() -> associadoService.create(new CriarAssociadoRequest("Maria", CPF)))
        .isInstanceOf(ConflitoException.class)
        .hasMessage("Já existe um associado com este CPF");

    verify(associadoRepository, never()).saveAndFlush(any());
  }

  @Test
  @DisplayName("Deve lançar exceção quando o banco rejeitar CPF duplicado por concorrência")
  void deveRejeitarCpfDuplicadoConcorrente() {
    when(associadoRepository.existsByCpf(CPF)).thenReturn(false);
    when(associadoRepository.saveAndFlush(any(Associado.class)))
        .thenThrow(new DataIntegrityViolationException("uk_associado_cpf"));

    assertThatThrownBy(() -> associadoService.create(new CriarAssociadoRequest("Maria", CPF)))
        .isInstanceOf(ConflitoException.class)
        .hasMessage("Já existe um associado com este CPF");
  }

  @Test
  @DisplayName("Deve buscar o associado pelo CPF, ignorando a pontuação")
  void deveBuscarPorCpf() {
    Associado associado = new Associado("Maria", CPF);
    when(associadoRepository.findByCpf(CPF)).thenReturn(Optional.of(associado));

    assertThat(associadoService.getByCpf("529.982.247-25")).isSameAs(associado);
  }

  @Test
  @DisplayName("Deve lançar exceção quando não houver associado com o CPF")
  void deveRejeitarCpfInexistente() {
    when(associadoRepository.findByCpf(CPF)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> associadoService.getByCpf(CPF))
        .isInstanceOf(RecursoNaoEncontradoException.class)
        .hasMessage("Associado não encontrado");
  }

  @Test
  @DisplayName("Deve retornar todos os associados cadastrados")
  void deveListarTodosAssociados() {
    when(associadoRepository.findAll()).thenReturn(List.of(
        new Associado("Maria", CPF),
        new Associado("João", "11144477735")));

    List<Associado> resultado = associadoService.getAllAssociados();

    assertThat(resultado).extracting(Associado::getNome).containsExactly("Maria", "João");
  }

  @Test
  @DisplayName("Deve retornar lista vazia quando não houver associados")
  void deveRetornarListaVazia() {
    when(associadoRepository.findAll()).thenReturn(List.of());

    assertThat(associadoService.getAllAssociados()).isEmpty();
  }
}
