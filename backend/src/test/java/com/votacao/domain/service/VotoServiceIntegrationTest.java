package com.votacao.domain.service;

import com.votacao.domain.model.Pauta;
import com.votacao.domain.model.SessaoVotacao;
import com.votacao.domain.model.Voto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class VotoServiceIntegrationTest {

    @Autowired
    private VotoService votoService;

    @Autowired
    private SessaoVotacaoService sessaoVotacaoService;

    @Autowired
    private PautaService pautaService;

    @Test
    void deveRegistrarVotoNoBanco() {
        Pauta pauta = pautaService.save(Pauta.builder().titulo("Pauta Teste").descricao("Descrição Teste").build());
        SessaoVotacao sessao = sessaoVotacaoService.save(SessaoVotacao.builder().pauta(pauta).inicio(LocalDateTime.now()).fim(LocalDateTime.now().plusMinutes(10)).aberta(true).build());
        Voto voto = Voto.builder().pauta(pauta).sessaoVotacao(sessao).escolha("Sim").voto(true).build();

        Voto salvo = votoService.save(voto);

        assertThat(salvo.getId()).isNotNull();
        assertThat(salvo.getSessaoVotacao().getId()).isEqualTo(sessao.getId());
        assertThat(salvo.isVoto()).isTrue();
    }

}