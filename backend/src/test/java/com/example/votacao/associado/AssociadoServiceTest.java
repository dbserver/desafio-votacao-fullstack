package com.example.votacao.associado;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.votacao.associado.dto.CriarAssociadoRequest;

@ExtendWith(MockitoExtension.class)
class AssociadoServiceTest {

  @Mock
  private AssociadoRepository associadoRepository;

  @InjectMocks
  private AssociadoService associadoService;

  @Test
  @DisplayName("Deve salvar o associado com o nome informado")
  void deveCriarAssociado() {
    when(associadoRepository.save(any(Associado.class))).thenAnswer(invocation -> invocation.getArgument(0));

    Associado associado = associadoService.create(new CriarAssociadoRequest("Maria"));

    ArgumentCaptor<Associado> captor = ArgumentCaptor.forClass(Associado.class);
    verify(associadoRepository).save(captor.capture());
    assertThat(captor.getValue().getNome()).isEqualTo("Maria");
    assertThat(associado).isSameAs(captor.getValue());
  }

  @Test
  @DisplayName("Deve retornar todos os associados cadastrados")
  void deveListarTodosAssociados() {
    when(associadoRepository.findAll()).thenReturn(List.of(new Associado("Maria"), new Associado("João")));

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
