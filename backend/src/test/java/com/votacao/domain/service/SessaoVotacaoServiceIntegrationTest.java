package com.votacao.domain.service;

import com.votacao.domain.model.Pauta;
import com.votacao.domain.model.SessaoVotacao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Transactional
class SessaoVotacaoServiceIntegrationTest {

    @Autowired
    private SessaoVotacaoService sessaoVotacaoService;

    @Autowired
    private PautaService pautaService;

    @Test
    void deveSalvarSessaoVotacaoNoBanco() {
        Pauta pauta = pautaService.save(Pauta.builder().titulo("Pauta Teste").descricao("Descrição Teste").build());
        SessaoVotacao sessao = SessaoVotacao.builder().pauta(pauta).inicio(LocalDateTime.now()).fim(LocalDateTime.now().plusMinutes(10)).aberta(true).build();

        SessaoVotacao salvo = sessaoVotacaoService.save(sessao);

        assertThat(salvo.getId()).isNotNull();
        assertThat(salvo.getPauta().getId()).isEqualTo(pauta.getId());
        assertThat(salvo.isAberta()).isTrue();
    }
}