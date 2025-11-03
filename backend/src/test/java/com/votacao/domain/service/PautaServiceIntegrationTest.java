package com.votacao.domain.service;

import com.votacao.application.dto.PautaRequestDTO;
import com.votacao.domain.model.Pauta;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class PautaServiceIntegrationTest {

    @Autowired
    private PautaService pautaService;

    @Test
    void deveSalvarPautaNoBanco() {
        PautaRequestDTO dto = PautaRequestDTO.builder().titulo("Integração Teste").descricao("Descrição integração").build();
        Pauta pauta = Pauta.builder().titulo(dto.getTitulo()).descricao(dto.getDescricao()).build();

        Pauta salvo = pautaService.save(pauta);

        assertThat(salvo.getId()).isNotNull();
        assertThat(salvo.getTitulo()).isEqualTo(dto.getTitulo());
    }

}