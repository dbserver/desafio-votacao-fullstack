package com.dbserver.votacao.service;

import com.dbserver.votacao.dto.PautaRequest;
import com.dbserver.votacao.dto.PautaResponse;
import com.dbserver.votacao.entity.Pauta;
import com.dbserver.votacao.exception.NotFoundException;
import com.dbserver.votacao.repository.PautaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PautaServiceTest {

    @Mock
    private PautaRepository pautaRepository;

    @InjectMocks
    private PautaService pautaService;

    @Test
    void deveCriarPautaComSucesso() {
        PautaRequest request = new PautaRequest("Pauta Teste", "Descrição");
        Pauta pauta = Pauta.builder()
                .id(1L)
                .titulo("Pauta Teste")
                .descricao("Descrição")
                .criadaEm(LocalDateTime.now())
                .build();

        when(pautaRepository.save(any(Pauta.class))).thenReturn(pauta);

        PautaResponse response = pautaService.criar(request);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.titulo()).isEqualTo("Pauta Teste");
        assertThat(response.descricao()).isEqualTo("Descrição");
    }

    @Test
    void deveListarTodasPautas() {
        Pauta pauta = Pauta.builder()
                .id(1L)
                .titulo("Pauta 1")
                .criadaEm(LocalDateTime.now())
                .build();

        when(pautaRepository.findAll()).thenReturn(List.of(pauta));

        List<PautaResponse> resultado = pautaService.listarTodas();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).titulo()).isEqualTo("Pauta 1");
    }

    @Test
    void deveBuscarPautaPorId() {
        Pauta pauta = Pauta.builder()
                .id(1L)
                .titulo("Pauta 1")
                .criadaEm(LocalDateTime.now())
                .build();

        when(pautaRepository.findById(1L)).thenReturn(Optional.of(pauta));

        PautaResponse response = pautaService.buscarPorId(1L);

        assertThat(response.id()).isEqualTo(1L);
    }

    @Test
    void deveLancarExcecaoQuandoPautaNaoEncontrada() {
        when(pautaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> pautaService.buscarPorId(99L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Pauta não encontrada");
    }
}
