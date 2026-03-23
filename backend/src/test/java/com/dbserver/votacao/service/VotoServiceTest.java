package com.dbserver.votacao.service;

import com.dbserver.votacao.client.CpfValidationClient;
import com.dbserver.votacao.dto.CpfValidationResponse;
import com.dbserver.votacao.dto.ResultadoVotacaoResponse;
import com.dbserver.votacao.dto.VotoRequest;
import com.dbserver.votacao.dto.VotoResponse;
import com.dbserver.votacao.entity.Pauta;
import com.dbserver.votacao.entity.SessaoVotacao;
import com.dbserver.votacao.entity.Voto;
import com.dbserver.votacao.enums.VotoOpcao;
import com.dbserver.votacao.exception.BusinessException;
import com.dbserver.votacao.repository.VotoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VotoServiceTest {

    @Mock
    private VotoRepository votoRepository;

    @Mock
    private SessaoVotacaoService sessaoVotacaoService;

    @Mock
    private CpfValidationClient cpfValidationClient;

    @InjectMocks
    private VotoService votoService;

    private SessaoVotacao criarSessaoAberta() {
        Pauta pauta = Pauta.builder().id(1L).titulo("Pauta Teste").criadaEm(LocalDateTime.now()).build();
        return SessaoVotacao.builder()
                .id(1L)
                .pauta(pauta)
                .dataInicio(LocalDateTime.now().minusMinutes(1))
                .dataFim(LocalDateTime.now().plusMinutes(10))
                .build();
    }

    private SessaoVotacao criarSessaoEncerrada() {
        Pauta pauta = Pauta.builder().id(1L).titulo("Pauta Teste").criadaEm(LocalDateTime.now()).build();
        return SessaoVotacao.builder()
                .id(1L)
                .pauta(pauta)
                .dataInicio(LocalDateTime.now().minusMinutes(10))
                .dataFim(LocalDateTime.now().minusMinutes(5))
                .build();
    }

    @Test
    void deveRegistrarVotoComSucesso() {
        SessaoVotacao sessao = criarSessaoAberta();
        Voto voto = Voto.builder()
                .id(1L)
                .sessao(sessao)
                .associadoId("12345678901")
                .voto(VotoOpcao.SIM)
                .build();

        when(sessaoVotacaoService.findByPautaId(1L)).thenReturn(sessao);
        when(votoRepository.existsBySessaoIdAndAssociadoId(1L, "12345678901")).thenReturn(false);
        when(cpfValidationClient.validarCpf(anyString())).thenReturn(new CpfValidationResponse("ABLE_TO_VOTE"));
        when(votoRepository.save(any(Voto.class))).thenReturn(voto);

        VotoRequest request = new VotoRequest("12345678901", VotoOpcao.SIM);
        VotoResponse response = votoService.votar(1L, request);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.voto()).isEqualTo(VotoOpcao.SIM);
    }

    @Test
    void deveLancarExcecaoQuandoSessaoEncerrada() {
        SessaoVotacao sessao = criarSessaoEncerrada();
        when(sessaoVotacaoService.findByPautaId(1L)).thenReturn(sessao);

        VotoRequest request = new VotoRequest("12345678901", VotoOpcao.SIM);

        assertThatThrownBy(() -> votoService.votar(1L, request))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("não está aberta");
    }

    @Test
    void deveLancarExcecaoQuandoAssociadoJaVotou() {
        SessaoVotacao sessao = criarSessaoAberta();
        when(sessaoVotacaoService.findByPautaId(1L)).thenReturn(sessao);
        when(votoRepository.existsBySessaoIdAndAssociadoId(1L, "12345678901")).thenReturn(true);

        VotoRequest request = new VotoRequest("12345678901", VotoOpcao.SIM);

        assertThatThrownBy(() -> votoService.votar(1L, request))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("já votou");
    }

    @Test
    void deveRetornarResultadoAprovada() {
        SessaoVotacao sessao = criarSessaoAberta();
        when(sessaoVotacaoService.findByPautaId(1L)).thenReturn(sessao);
        when(votoRepository.countBySessaoIdAndVoto(1L, VotoOpcao.SIM)).thenReturn(7L);
        when(votoRepository.countBySessaoIdAndVoto(1L, VotoOpcao.NAO)).thenReturn(3L);

        ResultadoVotacaoResponse response = votoService.resultado(1L);

        assertThat(response.totalVotos()).isEqualTo(10);
        assertThat(response.votosSim()).isEqualTo(7);
        assertThat(response.votosNao()).isEqualTo(3);
        assertThat(response.resultado()).isEqualTo("APROVADA");
    }

    @Test
    void deveRetornarResultadoReprovada() {
        SessaoVotacao sessao = criarSessaoAberta();
        when(sessaoVotacaoService.findByPautaId(1L)).thenReturn(sessao);
        when(votoRepository.countBySessaoIdAndVoto(1L, VotoOpcao.SIM)).thenReturn(2L);
        when(votoRepository.countBySessaoIdAndVoto(1L, VotoOpcao.NAO)).thenReturn(8L);

        ResultadoVotacaoResponse response = votoService.resultado(1L);

        assertThat(response.resultado()).isEqualTo("REPROVADA");
    }

    @Test
    void deveRetornarResultadoEmpate() {
        SessaoVotacao sessao = criarSessaoAberta();
        when(sessaoVotacaoService.findByPautaId(1L)).thenReturn(sessao);
        when(votoRepository.countBySessaoIdAndVoto(1L, VotoOpcao.SIM)).thenReturn(5L);
        when(votoRepository.countBySessaoIdAndVoto(1L, VotoOpcao.NAO)).thenReturn(5L);

        ResultadoVotacaoResponse response = votoService.resultado(1L);

        assertThat(response.resultado()).isEqualTo("EMPATE");
    }
}
