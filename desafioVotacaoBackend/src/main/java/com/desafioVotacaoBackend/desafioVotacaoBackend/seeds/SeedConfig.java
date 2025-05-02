package com.desafioVotacaoBackend.desafioVotacaoBackend.seeds;

import com.desafioVotacaoBackend.desafioVotacaoBackend.model.Associado;
import com.desafioVotacaoBackend.desafioVotacaoBackend.model.Pauta;
import com.desafioVotacaoBackend.desafioVotacaoBackend.repository.AssociadoRepository;
import com.desafioVotacaoBackend.desafioVotacaoBackend.repository.PautaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SeedConfig {

    @Bean
    public CommandLineRunner seedDatabase(
            AssociadoRepository associadoRepository,
            PautaRepository pautaRepository) {
        return args -> {
            if (associadoRepository.count() == 0) {
                associadoRepository.saveAll(List.of(
                        Associado.builder().cpf("12345678900").nome("Maria Silva").build(),
                        Associado.builder().cpf("98765432100").nome("João Souza").build(),
                        Associado.builder().cpf("11122233344").nome("Ana Costa").build()));
            }

            if (pautaRepository.count() == 0) {
                Pauta pauta1 = new Pauta("Reforma da sede", "Votação para aprovar a reforma da sede.");
                pauta1.abrirSessao(60);
                Pauta pauta2 = new Pauta("Novo regimento interno", "Discussão e votação sobre o novo regimento.");
                pauta2.abrirSessao(60);

                pautaRepository.saveAll(List.of(pauta1, pauta2));
            }
        };
    }
}
