package dev.rodrigo.desafiovotacao.service;

import dev.rodrigo.desafiovotacao.dto.PautaRequestDto;
import dev.rodrigo.desafiovotacao.entity.Pauta;
import dev.rodrigo.desafiovotacao.exceptions.RecursoNaoEncontradoException;
import dev.rodrigo.desafiovotacao.repository.PautaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PautaServiceTest {

  @Mock private PautaRepository repository;

  @InjectMocks private PautaService service;

  @Test
  void deveCriarNovaPauta() {
    PautaRequestDto dto = new PautaRequestDto();
    dto.setTitulo("Nova pauta");
    dto.setDescricao("Descrição");

    service.criarNovaPauta(dto);

    verify(repository, times(1)).save(any(Pauta.class));
  }

  @Test
  void deveBuscarPautaPorId() {
    Pauta pauta = new Pauta();
    pauta.setId(1L);

    when(repository.findById(1L)).thenReturn(Optional.of(pauta));

    Pauta resultado = service.buscarPautaPorId(1L);

    assertEquals(1L, resultado.getId());
  }

  @Test
  void deveLancarExcecaoQuandoPautaNaoExiste() {
    when(repository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(RecursoNaoEncontradoException.class, () -> service.buscarPautaPorId(1L));
  }

  @Test
  void deveRetornarTodasAsPautas() {
    List<Pauta> pautaList = new ArrayList<>();
    Pauta pauta = new Pauta();
    pauta.setId(1L);
    pautaList.add(pauta);

    when(repository.findAll()).thenReturn(pautaList);

    List<Pauta> resultado = service.buscarPautas();

    assertThat(resultado).isNotNull();
    assertThat(resultado).hasSize(1);
    assertThat(resultado.getFirst().getId()).isEqualTo(1L);

    verify(repository).findAll();
  }
}
