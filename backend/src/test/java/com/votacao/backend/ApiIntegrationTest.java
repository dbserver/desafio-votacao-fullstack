package com.votacao.backend;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.votacao.backend.domain.entity.Sessao;
import com.votacao.backend.domain.repository.PautaRepository;
import com.votacao.backend.domain.repository.SessaoRepository;
import com.votacao.backend.domain.repository.VotoRepository;
import com.votacao.backend.infrastructure.cpf.CpfStatus;
import com.votacao.backend.infrastructure.logging.RequestLoggingFilter;
import com.votacao.backend.infrastructure.time.AppDateTime;
import com.votacao.backend.support.IntegrationTestConfig;
import com.votacao.backend.support.MutableCpfValidationClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.time.Duration;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@Import(IntegrationTestConfig.class)
class ApiIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MutableCpfValidationClient cpfValidationClient;

    @Autowired
    private PautaRepository pautaRepository;

    @Autowired
    private SessaoRepository sessaoRepository;

    @Autowired
    private VotoRepository votoRepository;

    @Autowired
    private RequestLoggingFilter requestLoggingFilter;

    @BeforeEach
    void prepararEstado() {
        mockMvc = webAppContextSetup(webApplicationContext)
                .addFilters(requestLoggingFilter)
                .build();
        cpfValidationClient.limpar();
        votoRepository.deleteAll();
        sessaoRepository.deleteAll();
        pautaRepository.deleteAll();
    }

    @Test
    void deveCriarPautaEListarDeFormaPaginada() throws Exception {
        criarPauta("Pauta 1", "Descricao 1");
        criarPauta("Pauta 2", "Descricao 2");

        String respostaLista = mockMvc.perform(get("/api/v1/pautas?page=0&size=1"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode pagina = objectMapper.readTree(respostaLista);
        assertEquals(2, pagina.path("totalElements").asInt());
        assertEquals(2, pagina.path("totalPages").asInt());
        assertEquals(1, pagina.path("content").size());
        assertTrue(pagina.path("first").asBoolean());
        assertFalse(pagina.path("last").asBoolean());
    }

    @Test
    void deveRetornarLocationAoCriarPauta() throws Exception {
        MvcResult result = mockMvc.perform(
                        post("/api/v1/pautas")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "titulo": "Pauta Location",
                                          "descricao": "Descricao"
                                        }
                                        """))
                .andExpect(status().isCreated())
                .andReturn();

        JsonNode pauta = objectMapper.readTree(result.getResponse().getContentAsString());
        String location = result.getResponse().getHeader("Location");

        assertNotNull(location);
        assertTrue(location.endsWith("/api/v1/pautas/" + pauta.path("id").asText()));
    }

    @Test
    void deveAbrirSessaoComDuracaoPadraoDeUmMinuto() throws Exception {
        String pautaId = criarPauta("Pauta Sessao", "Descricao");

        String respostaSessao = mockMvc.perform(post("/api/v1/pautas/{id}/sessao", pautaId))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode sessao = objectMapper.readTree(respostaSessao);
        LocalDateTime abertaEm = LocalDateTime.parse(sessao.path("abertaEm").asText());
        LocalDateTime expiraEm = LocalDateTime.parse(sessao.path("expiraEm").asText());

        long segundos = Duration.between(abertaEm, expiraEm).toSeconds();
        assertTrue(segundos >= 59 && segundos <= 61);
        assertFalse(sessao.path("id").asText().isBlank());
    }

    @Test
    void deveRetornarLocationAoAbrirSessaoEBuscarPorId() throws Exception {
        String pautaId = criarPauta("Pauta Sessao Location", "Descricao");

        MvcResult result = mockMvc.perform(post("/api/v1/pautas/{id}/sessao", pautaId))
                .andExpect(status().isCreated())
                .andReturn();

        JsonNode sessao = objectMapper.readTree(result.getResponse().getContentAsString());
        String location = result.getResponse().getHeader("Location");
        String sessaoId = sessao.path("id").asText();

        assertNotNull(location);
        assertTrue(location.endsWith("/api/v1/sessoes/" + sessaoId));

        String respostaBusca = mockMvc.perform(get("/api/v1/sessoes/{id}", sessaoId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode sessaoBuscada = objectMapper.readTree(respostaBusca);
        assertEquals(sessaoId, sessaoBuscada.path("id").asText());
        assertEquals(pautaId, sessaoBuscada.path("pautaId").asText());
    }

    @Test
    void deveRetornarStatusDoCpfEAceitarVoto() throws Exception {
        String pautaId = criarPauta("Pauta Voto", "Descricao");
        String sessaoId = abrirSessao(pautaId, 5);
        String cpf = "12345678901";

        cpfValidationClient.marcarValido(cpf, CpfStatus.ABLE_TO_VOTE);

        String respostaCpf = mockMvc.perform(get("/api/v1/cpfs/{cpf}", cpf))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode cpfJson = objectMapper.readTree(respostaCpf);
        assertEquals("ABLE_TO_VOTE", cpfJson.path("status").asText());

        MvcResult resultadoVoto = mockMvc.perform(
                        post("/api/v1/sessoes/{sessaoId}/votos", sessaoId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {"associadoId":"12345678901","opcao":"SIM"}
                                        """))
                .andExpect(status().isCreated())
                .andReturn();

        JsonNode voto = objectMapper.readTree(resultadoVoto.getResponse().getContentAsString());
        String votoId = voto.path("id").asText();
        String votoLocation = resultadoVoto.getResponse().getHeader("Location");

        assertFalse(votoId.isBlank());
        assertNotNull(votoLocation);
        assertTrue(votoLocation.endsWith("/api/v1/sessoes/" + sessaoId + "/votos/" + votoId));

        String respostaVoto = mockMvc.perform(get("/api/v1/sessoes/{sessaoId}/votos/{votoId}", sessaoId, votoId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode votoBuscado = objectMapper.readTree(respostaVoto);
        assertEquals(votoId, votoBuscado.path("id").asText());
        assertEquals("12345678901", votoBuscado.path("associadoId").asText());
        assertEquals("SIM", votoBuscado.path("opcao").asText());

        String respostaResultado = mockMvc.perform(get("/api/v1/sessoes/{sessaoId}/resultado", sessaoId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode resultado = objectMapper.readTree(respostaResultado);
        assertEquals(1, resultado.path("sim").asInt());
        assertEquals(0, resultado.path("nao").asInt());
        assertEquals(1, resultado.path("total").asInt());
        assertFalse(resultado.path("encerrada").asBoolean());
    }

    @Test
    void deveRetornarLocationAoRegistrarVoto() throws Exception {
        String pautaId = criarPauta("Pauta Vote Location", "Descricao");
        String sessaoId = abrirSessao(pautaId, 5);
        String cpf = "99887766554";

        cpfValidationClient.marcarValido(cpf, CpfStatus.ABLE_TO_VOTE);

        MvcResult result = mockMvc.perform(
                        post("/api/v1/sessoes/{sessaoId}/votos", sessaoId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {"associadoId":"99887766554","opcao":"NAO"}
                                        """))
                .andExpect(status().isCreated())
                .andReturn();

        JsonNode voto = objectMapper.readTree(result.getResponse().getContentAsString());
        String location = result.getResponse().getHeader("Location");

        assertNotNull(location);
        assertTrue(location.endsWith("/api/v1/sessoes/" + sessaoId + "/votos/" + voto.path("id").asText()));
    }

    @Test
    void deveBloquearVotoDuplicado() throws Exception {
        String pautaId = criarPauta("Pauta Duplicada", "Descricao");
        String sessaoId = abrirSessao(pautaId, 5);
        String cpf = "98765432100";

        cpfValidationClient.marcarValido(cpf, CpfStatus.ABLE_TO_VOTE);

        mockMvc.perform(
                        post("/api/v1/sessoes/{sessaoId}/votos", sessaoId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {"associadoId":"98765432100","opcao":"NAO"}
                                        """))
                .andExpect(status().isCreated());

        mockMvc.perform(
                        post("/api/v1/sessoes/{sessaoId}/votos", sessaoId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {"associadoId":"98765432100","opcao":"SIM"}
                                        """))
                .andExpect(status().isConflict());
    }

    @Test
    void deveBloquearVotoQuandoCpfNaoAutorizado() throws Exception {
        String pautaId = criarPauta("Pauta Bloqueio", "Descricao");
        String sessaoId = abrirSessao(pautaId, 5);
        String cpf = "11122233344";

        cpfValidationClient.marcarValido(cpf, CpfStatus.UNABLE_TO_VOTE);

        mockMvc.perform(
                        post("/api/v1/sessoes/{sessaoId}/votos", sessaoId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {"associadoId":"11122233344","opcao":"SIM"}
                                        """))
                .andExpect(status().isForbidden());
    }

    @Test
    void deveRetornar404QuandoSessaoNaoExistirAoRegistrarVoto() throws Exception {
        mockMvc.perform(
                        post("/api/v1/sessoes/{sessaoId}/votos", UUID.randomUUID())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {"associadoId":"12345678901","opcao":"SIM"}
                                        """))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveRetornar422QuandoSessaoEstiverEncerradaAoRegistrarVoto() throws Exception {
        String pautaId = criarPauta("Pauta Encerrada", "Descricao");
        String sessaoId = abrirSessao(pautaId, 1);

        Sessao sessao = sessaoRepository.findById(UUID.fromString(sessaoId)).orElseThrow();
        sessao.setExpiraEm(AppDateTime.now().minusMinutes(1));
        sessao.setEncerradaEm(AppDateTime.now().minusMinutes(1));
        sessaoRepository.saveAndFlush(sessao);

        mockMvc.perform(
                        post("/api/v1/sessoes/{sessaoId}/votos", sessaoId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {"associadoId":"12345678901","opcao":"SIM"}
                                        """))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void deveRetornar404QuandoCpfForInvalido() throws Exception {
        String cpf = "55566677788";
        cpfValidationClient.marcarInvalido(cpf);

        mockMvc.perform(get("/api/v1/cpfs/{cpf}", cpf))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveRetornar404QuandoVotoNaoExistirNaSessao() throws Exception {
        String pautaId = criarPauta("Pauta Voto Inexistente", "Descricao");
        String sessaoId = abrirSessao(pautaId, 5);

        mockMvc.perform(get("/api/v1/sessoes/{sessaoId}/votos/{votoId}", sessaoId, UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveRetornarRequestIdNaResposta() throws Exception {
        mockMvc.perform(get("/api/v1/pautas")
                        .header("X-Request-Id", "request-123"))
                .andExpect(status().isOk())
                .andExpect(header().string("X-Request-Id", "request-123"));
    }

    private String criarPauta(String titulo, String descricao) throws Exception {
        String resposta = mockMvc.perform(
                        post("/api/v1/pautas")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "titulo": "%s",
                                          "descricao": "%s"
                                        }
                                        """.formatted(titulo, descricao)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return objectMapper.readTree(resposta).path("id").asText();
    }

    private String abrirSessao(String pautaId, Integer duracaoMinutos) throws Exception {
        String resposta = mockMvc.perform(
                        post("/api/v1/pautas/{id}/sessao", pautaId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {"duracaoMinutos": %d}
                                        """.formatted(duracaoMinutos)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return objectMapper.readTree(resposta).path("id").asText();
    }
}
