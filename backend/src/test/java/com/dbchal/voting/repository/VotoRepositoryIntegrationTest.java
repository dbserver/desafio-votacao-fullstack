package com.dbchal.voting.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import com.dbchal.voting.dto.voto.ResultadoVotosProjection;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class VotoRepositoryIntegrationTest {

	@Container
	@ServiceConnection
	static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:16-alpine").withDatabaseName("voting_test")
			.withUsername("postgres").withPassword("postgres");

	@Autowired
	private VotoRepository votoRepository;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@BeforeEach
	void setup() {
		jdbcTemplate.update("""
				INSERT INTO pautas (
					id,
					titulo,
					descricao,
					dth_criacao
				)
				VALUES (
					1,
					'Pauta de teste',
					'Teste de descrição',
					CURRENT_TIMESTAMP
				)
				""");

		jdbcTemplate.update("""
				INSERT INTO sessao_votacao (
					id,
					pauta_id,
					dth_inicio,
					dth_fim
				)
				VALUES (
					1,
					1,
					CURRENT_TIMESTAMP - INTERVAL '1 minute',
					CURRENT_TIMESTAMP + INTERVAL '10 minutes'
				)
				""");

		jdbcTemplate.update("""
				INSERT INTO associados (
					id,
					cpf,
					dth_criacao
				)
				VALUES
					(1, '12345678901', CURRENT_TIMESTAMP),
					(2, '01987654321', CURRENT_TIMESTAMP),
					(3, '13278901456', CURRENT_TIMESTAMP)
				""");

		jdbcTemplate.update("""
				INSERT INTO votos (
					sessao_id,
					associado_id,
					opcao,
					dth_voto
				)
				VALUES
					(1, 1, 'SIM', CURRENT_TIMESTAMP),
					(1, 2, 'SIM', CURRENT_TIMESTAMP),
					(1, 3, 'NAO', CURRENT_TIMESTAMP)
				""");
	}

	@Test
	void shouldCountVotesBySession() {
		ResultadoVotosProjection resultado = votoRepository.countBySessaoId(1L);

		assertEquals(2L, resultado.getVotosSim());
		assertEquals(1L, resultado.getVotosNao());
	}

	@Test
	void shouldVerifyWhetherMemberAlreadyVoted() {
		assertTrue(votoRepository.existsBySessaoIdAndAssociadoId(1L, 1L));
		assertFalse(votoRepository.existsBySessaoIdAndAssociadoId(1L, 999L));
	}
}