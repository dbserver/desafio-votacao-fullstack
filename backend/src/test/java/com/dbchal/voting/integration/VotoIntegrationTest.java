package com.dbchal.voting.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import com.dbchal.voting.domain.enums.CpfValidationStatus;
import com.dbchal.voting.exception.associado.InvalidCpfException;
import com.dbchal.voting.facade.CpfValidationFacade;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class VotoIntegrationTest {

	@Container
	@ServiceConnection
	@SuppressWarnings("resource")
	static final PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:16-alpine")
														.withDatabaseName("voting_test")
														.withUsername("postgres")
														.withPassword("postgres");

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@MockitoBean
	private CpfValidationFacade cpfValidationFacade;

	@BeforeEach
	void setup() {
		jdbcTemplate.execute("""
				TRUNCATE TABLE votos, sessao_votacao, associados, pautas
				RESTART IDENTITY CASCADE
				""");

		when(cpfValidationFacade.validate(anyString()))
				.thenReturn(CpfValidationStatus.ABLE_TO_VOTE);
	}

	@Test
	void shouldRegisterVote() throws Exception {
		insertPauta(1L);
		insertOpenSession(1L, 1L);
		insertAssociado(1L, "98865754031");

		mockMvc.perform(post("/api/v1/votos/{pautaId}", 1L).contentType(MediaType.APPLICATION_JSON).content("""
				{
				  "associadoId": 1,
				  "opcao": "SIM"
				}
				""")).andExpect(status().isCreated()).andExpect(jsonPath("$.id").isNumber())
				.andExpect(jsonPath("$.sessaoId").value(1)).andExpect(jsonPath("$.pautaId").value(1))
				.andExpect(jsonPath("$.associadoId").value(1)).andExpect(jsonPath("$.opcao").value("SIM"))
				.andExpect(jsonPath("$.dthVoto").isNotEmpty());

		assertEquals(1L, countVotes());
	}

	@Test
	void shouldNotAllowDuplicateVoteInSameSession() throws Exception {
		insertPauta(1L);
		insertOpenSession(1L, 1L);
		insertAssociado(1L, "01234567890");

		String request = """
				{
				  "associadoId": 1,
				  "opcao": "SIM"
				}
				""";

		mockMvc.perform(post("/api/v1/votos/{pautaId}", 1L)
					.contentType(MediaType.APPLICATION_JSON).content(request))
						.andExpect(status().isCreated());

		mockMvc.perform(post("/api/v1/votos/{pautaId}", 1L)
					.contentType(MediaType.APPLICATION_JSON).content(request))
						.andExpect(status().isConflict())
						.andExpect(jsonPath("$.status").value(409))
						.andExpect(jsonPath("$.erro").value("DUPLICATED_VOTE"))
						.andExpect(jsonPath("$.caminho").value("/api/v1/votos/1"));

		assertEquals(1L, countVotes());
	}

	@Test
	void shouldNotAllowVoteWhenSessionIsExpired() throws Exception {
		insertPauta(1L);
		insertExpiredSession(1L, 1L);
		insertAssociado(1L, "98765432101");

		mockMvc.perform(post("/api/v1/votos/{pautaId}", 1L).contentType(MediaType.APPLICATION_JSON).content("""
				{
				  "associadoId": 1,
				  "opcao": "SIM"
				}
				""")).andExpect(status().isForbidden())
					 .andExpect(jsonPath("$.status").value(403))
					 .andExpect(jsonPath("$.erro").value("EXPIRED_SESSION"));

		assertEquals(0L, countVotes());
	}

	@Test
	void shouldReturnNotFoundWhenAssociadoDoesNotExist() throws Exception {
		insertPauta(1L);
		insertOpenSession(1L, 1L);

		mockMvc.perform(post("/api/v1/votos/{pautaId}", 1L).contentType(MediaType.APPLICATION_JSON).content("""
				{
				  "associadoId": 999,
				  "opcao": "NAO"
				}
				""")).andExpect(status().isNotFound())
					 .andExpect(jsonPath("$.status").value(404))
					 .andExpect(jsonPath("$.erro").value("ASSOCIADO_NOT_FOUND"));

		assertEquals(0L, countVotes());
	}

	@Test
	void shouldReturnNotFoundWhenVotingSessionDoesNotExist() throws Exception {
		insertPauta(1L);
		insertAssociado(1L, "03888756048");

		mockMvc.perform(post("/api/v1/votos/{pautaId}", 1L).contentType(MediaType.APPLICATION_JSON).content("""
				{
				  "associadoId": 1,
				  "opcao": "SIM"
				}
				""")).andExpect(status().isNotFound())
					 .andExpect(jsonPath("$.status").value(404))
					 .andExpect(jsonPath("$.erro").value("SESSAO_NOT_FOUND"));

		assertEquals(0L, countVotes());
	}

	@Test
	void shouldReturnBadRequestWhenRequestIsInvalid() throws Exception {
		insertPauta(1L);
		insertOpenSession(1L, 1L);

		mockMvc.perform(post("/api/v1/votos/{pautaId}", 1L).contentType(MediaType.APPLICATION_JSON).content("""
				{
				  "associadoId": 0
				}
				""")).andExpect(status().isBadRequest());

		assertEquals(0L, countVotes());
	}
	
	@Test
	void shouldNotRegisterVoteWhenAssociadoIsUnableToVote()
			throws Exception {

		insertPauta(1L);
		insertOpenSession(1L, 1L);
		insertAssociado(1L, "52998224725");

		when(cpfValidationFacade.validate("52998224725"))
				.thenReturn(CpfValidationStatus.UNABLE_TO_VOTE);

		mockMvc.perform(
				post("/api/v1/votos/{pautaId}", 1L)
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{
								  "associadoId": 1,
								  "opcao": "SIM"
								}
								""")).andExpect(status().isForbidden())
									 .andExpect(jsonPath("$.status").value(403))
									 .andExpect(jsonPath("$.erro").value("UNABLE_TO_VOTE"))
									 .andExpect(jsonPath("$.caminho").value("/api/v1/votos/1"));

		assertEquals(0L, countVotes());
	}

	@Test
	void shouldReturnNotFoundWhenCpfIsInvalid()
			throws Exception {

		insertPauta(1L);
		insertOpenSession(1L, 1L);
		insertAssociado(1L, "11111111111");

		when(cpfValidationFacade.validate("11111111111"))
				.thenThrow(new InvalidCpfException());

		mockMvc.perform(
				post("/api/v1/votos/{pautaId}", 1L)
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{
								  "associadoId": 1,
								  "opcao": "SIM"
								}
								""")).andExpect(status().isNotFound())
									 .andExpect(jsonPath("$.status").value(404))
									 .andExpect(jsonPath("$.erro").value("CPF_NOT_FOUND"))
									 .andExpect(jsonPath("$.caminho").value("/api/v1/votos/1"));

		assertEquals(0L, countVotes());
	}

	private void insertPauta(Long id) {
		jdbcTemplate.update("""
				INSERT INTO pautas (id, titulo, descricao, dth_criacao)
				VALUES (?, 'Pauta de teste', 'Descrição de teste', CURRENT_TIMESTAMP)
				""", id);
	}

	private void insertOpenSession(Long id, Long pautaId) {
		jdbcTemplate.update("""
				INSERT INTO sessao_votacao (id, pauta_id, dth_inicio, dth_fim)
				VALUES (
					?,
					?,
					CURRENT_TIMESTAMP - INTERVAL '1 minute',
					CURRENT_TIMESTAMP + INTERVAL '10 minutes'
				)
				""", id, pautaId);
	}

	private void insertExpiredSession(Long id, Long pautaId) {
		jdbcTemplate.update("""
				INSERT INTO sessao_votacao (id, pauta_id, dth_inicio, dth_fim)
				VALUES (
					?,
					?,
					CURRENT_TIMESTAMP - INTERVAL '10 minutes',
					CURRENT_TIMESTAMP - INTERVAL '1 minute'
				)
				""", id, pautaId);
	}

	private void insertAssociado(Long id, String cpf) {
		jdbcTemplate.update("""
				INSERT INTO associados (id, cpf, dth_criacao)
				VALUES (?, ?, CURRENT_TIMESTAMP)
				""", id, cpf);
	}

	private long countVotes() {
		Long total = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM votos", Long.class);

		return total == null ? 0L : total;
	}
}
