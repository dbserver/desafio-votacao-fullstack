package com.example.votacao.pauta;

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

import com.example.votacao.pauta.dto.CriarPautaRequest;

@ExtendWith(MockitoExtension.class)
class PautaServiceTest {

  @Mock
  private PautaRepository pautaRepository;

  @InjectMocks
  private PautaService pautaService;

  @Test
  @DisplayName("Deve salvar a pauta com título, descrição e data de criação")
  void deveCriarPauta() {
    when(pautaRepository.save(any(Pauta.class))).thenAnswer(invocation -> invocation.getArgument(0));

    Pauta pauta = pautaService.create(new CriarPautaRequest("Reforma do estatuto", "Aprovar a nova redação"));

    ArgumentCaptor<Pauta> captor = ArgumentCaptor.forClass(Pauta.class);
    verify(pautaRepository).save(captor.capture());
    assertThat(captor.getValue().getTitulo()).isEqualTo("Reforma do estatuto");
    assertThat(captor.getValue().getDescricao()).isEqualTo("Aprovar a nova redação");
    assertThat(captor.getValue().getCriadaEm()).isNotNull();
    assertThat(pauta).isSameAs(captor.getValue());
  }

  @Test
  @DisplayName("Deve retornar todas as pautas cadastradas")
  void deveListarTodasPautas() {
    when(pautaRepository.findAll()).thenReturn(List.of(
        new Pauta("Reforma do estatuto", "Aprovar a nova redação"),
        new Pauta("Eleição do conselho", "Escolher os novos conselheiros")));

    List<Pauta> resultado = pautaService.getAllPautas();

    assertThat(resultado).extracting(Pauta::getTitulo)
        .containsExactly("Reforma do estatuto", "Eleição do conselho");
  }

  @Test
  @DisplayName("Deve retornar lista vazia quando não houver pautas")
  void deveRetornarListaVazia() {
    when(pautaRepository.findAll()).thenReturn(List.of());

    assertThat(pautaService.getAllPautas()).isEmpty();
  }
}
