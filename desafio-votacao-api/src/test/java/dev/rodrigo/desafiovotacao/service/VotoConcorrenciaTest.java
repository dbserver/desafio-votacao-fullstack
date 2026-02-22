package dev.rodrigo.desafiovotacao.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import dev.rodrigo.desafiovotacao.dto.CpfValidationResponse;
import dev.rodrigo.desafiovotacao.dto.VotoRequestDto;
import dev.rodrigo.desafiovotacao.entity.Pauta;
import dev.rodrigo.desafiovotacao.entity.SessaoVotacao;
import dev.rodrigo.desafiovotacao.enums.StatusSessao;
import dev.rodrigo.desafiovotacao.enums.TipoVoto;
import dev.rodrigo.desafiovotacao.facade.CpfValidationClient;
import dev.rodrigo.desafiovotacao.repository.PautaRepository;
import dev.rodrigo.desafiovotacao.repository.SessaoVotacaoRepository;
import dev.rodrigo.desafiovotacao.repository.VotoRepository;
import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
class VotoConcorrenciaTest {

  @Autowired private VotoService votoService;

  @Autowired private VotoRepository votoRepository;

  @Autowired private SessaoVotacaoRepository sessaoRepository;

  @Autowired private PautaRepository pautaRepository;

  @MockitoBean private CpfValidationClient cpfValidationClient;

  @Test
  void devePermitirApenasUmVotoEmAltaConcorrencia() throws InterruptedException {

    Pauta pauta = new Pauta();
    pauta.setTitulo("Teste");
    pauta.setDescricao("Teste");

    pauta = pautaRepository.save(pauta);

    SessaoVotacao sessao = new SessaoVotacao();
    sessao.setDataAbertura(LocalDateTime.now());
    sessao.setDataFechamento(LocalDateTime.now().plusMinutes(10));
    sessao.setStatus(StatusSessao.ABERTA);
    sessao.setPauta(pauta);

    sessao = sessaoRepository.save(sessao);
    Long sessaoId = sessao.getId();
    String cpf = "430.691.550-65";

    int numeroThreads = 20;

    when(cpfValidationClient.validarCpf(any()))
        .thenReturn(new CpfValidationResponse("ABLE_TO_VOTE"));

    try (ExecutorService executor = Executors.newFixedThreadPool(numeroThreads)) {
      CountDownLatch latch = new CountDownLatch(numeroThreads);

      for (int i = 0; i < numeroThreads; i++) {
        executor.submit(
            () -> {
              try {
                VotoRequestDto request = new VotoRequestDto();
                request.setCpf(cpf);
                request.setVoto(TipoVoto.SIM);

                votoService.votar(sessaoId, request);

              } catch (Exception ignored) {
              } finally {
                latch.countDown();
              }
            });
      }

      latch.await();
      executor.shutdown();
    }

    long totalVotos = votoRepository.countBySessao_Id(sessaoId);

    assertEquals(1L, totalVotos);
  }
}
