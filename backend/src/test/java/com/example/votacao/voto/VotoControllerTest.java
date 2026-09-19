package com.example.votacao.voto;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Duration;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import com.example.votacao.associado.Associado;
import com.example.votacao.pauta.Pauta;
import com.example.votacao.sessao.Sessao;
import com.example.votacao.shared.ConflitoException;
import com.example.votacao.shared.RecursoNaoEncontradoException;
import com.example.votacao.voto.dto.CriarVotoRequest;

@WebMvcTest(VotoController.class)
class VotoControllerTest {

  private static final Long PAUTA_ID = 4L;
  private static final String URL = "/api/v1/pauta/" + PAUTA_ID + "/voto";

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private VotoService votoService;

  private Sessao sessao;
  private Associado associado;

  @BeforeEach
  void setUp() {
    Pauta pauta = new Pauta("Reforma do estatuto", "Aprovar a nova redação");
    ReflectionTestUtils.setField(pauta, "id", PAUTA_ID);

    sessao = new Sessao(pauta, Duration.ofMinutes(1));
    ReflectionTestUtils.setField(sessao, "id", 1L);

    associado = new Associado("Associado Fulano");
    ReflectionTestUtils.setField(associado, "id", 3L);
  }

  @Test
  @DisplayName("POST deve retornar 201 com o voto registrado")
  void deveRegistrarVoto() throws Exception {
    Voto voto = new Voto(sessao, associado, OpcaoVoto.SIM);
    ReflectionTestUtils.setField(voto, "id", 10L);
    when(votoService.create(eq(PAUTA_ID), any(CriarVotoRequest.class))).thenReturn(voto);

    mockMvc.perform(post(URL)
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"associadoId\": 3, \"opcao\": \"SIM\"}"))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(10))
        .andExpect(jsonPath("$.sessaoId").value(1))
        .andExpect(jsonPath("$.associadoId").value(3))
        .andExpect(jsonPath("$.opcao").value("SIM"))
        .andExpect(jsonPath("$.criadoEm").exists());

    verify(votoService).create(PAUTA_ID, new CriarVotoRequest(3L, OpcaoVoto.SIM));
  }

  @Test
  @DisplayName("POST deve retornar 400 quando o associado não for enviado")
  void deveRejeitarAssociadoAusente() throws Exception {
    mockMvc.perform(post(URL)
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"opcao\": \"SIM\"}"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors.associadoId").value("Associado é obrigatório"));

    verify(votoService, never()).create(any(), any());
  }

  @Test
  @DisplayName("POST deve retornar 400 quando a opção não for enviada")
  void deveRejeitarOpcaoAusente() throws Exception {
    mockMvc.perform(post(URL)
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"associadoId\": 3}"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors.opcao").value("Opção é obrigatória"));

    verify(votoService, never()).create(any(), any());
  }

  @Test
  @DisplayName("POST deve retornar 400 quando a opção for inválida")
  void deveRejeitarOpcaoInvalida() throws Exception {
    mockMvc.perform(post(URL)
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"associadoId\": 3, \"opcao\": \"TALVEZ\"}"))
        .andExpect(status().isBadRequest());

    verify(votoService, never()).create(any(), any());
  }

  @Test
  @DisplayName("POST deve retornar 400 quando o corpo estiver vazio")
  void deveRejeitarCorpoVazio() throws Exception {
    mockMvc.perform(post(URL)
        .contentType(MediaType.APPLICATION_JSON)
        .content("{}"))
        .andExpect(status().isBadRequest());

    verify(votoService, never()).create(any(), any());
  }

  @Test
  @DisplayName("POST deve retornar 404 quando o associado não existir")
  void deveRetornar404AssociadoInexistente() throws Exception {
    when(votoService.create(eq(PAUTA_ID), any(CriarVotoRequest.class)))
        .thenThrow(new RecursoNaoEncontradoException("Associado não encontrado"));

    mockMvc.perform(post(URL)
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"associadoId\": 999, \"opcao\": \"SIM\"}"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value("Associado não encontrado"));
  }

  @Test
  @DisplayName("POST deve retornar 409 quando a sessão estiver encerrada")
  void deveRetornar409SessaoEncerrada() throws Exception {
    when(votoService.create(eq(PAUTA_ID), any(CriarVotoRequest.class)))
        .thenThrow(new ConflitoException("Sessão de votação encerrada"));

    mockMvc.perform(post(URL)
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"associadoId\": 3, \"opcao\": \"SIM\"}"))
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.message").value("Sessão de votação encerrada"));
  }

  @Test
  @DisplayName("POST deve retornar 409 quando o associado já tiver votado")
  void deveRetornar409VotoDuplicado() throws Exception {
    when(votoService.create(eq(PAUTA_ID), any(CriarVotoRequest.class)))
        .thenThrow(new ConflitoException("Associado já votou nesta pauta"));

    mockMvc.perform(post(URL)
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"associadoId\": 3, \"opcao\": \"NAO\"}"))
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.message").value("Associado já votou nesta pauta"));
  }

  @Test
  @DisplayName("GET deve retornar 200 com os votos da pauta")
  void deveListarVotos() throws Exception {
    when(votoService.getByPautaId(PAUTA_ID)).thenReturn(List.of(
        new Voto(sessao, associado, OpcaoVoto.SIM),
        new Voto(sessao, associado, OpcaoVoto.NAO)));

    mockMvc.perform(get(URL))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(2))
        .andExpect(jsonPath("$[0].opcao").value("SIM"))
        .andExpect(jsonPath("$[1].opcao").value("NAO"));
  }

  @Test
  @DisplayName("GET deve retornar 404 quando a pauta não tiver sessão")
  void deveRetornar404SemSessao() throws Exception {
    when(votoService.getByPautaId(PAUTA_ID))
        .thenThrow(new RecursoNaoEncontradoException("Sessão não encontrada para esta pauta"));

    mockMvc.perform(get(URL))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value("Sessão não encontrada para esta pauta"));
  }
}
