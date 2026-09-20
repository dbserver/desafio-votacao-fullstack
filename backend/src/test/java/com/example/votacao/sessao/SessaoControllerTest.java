package com.example.votacao.sessao;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Duration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import com.example.votacao.pauta.Pauta;
import com.example.votacao.sessao.dto.CriarSessaoRequest;
import com.example.votacao.shared.ConflitoException;
import com.example.votacao.shared.RecursoNaoEncontradoException;

@WebMvcTest(SessaoController.class)
class SessaoControllerTest {

  private static final Long PAUTA_ID = 4L;
  private static final String URL = "/api/v1/pauta/" + PAUTA_ID + "/sessao";

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private SessaoService sessaoService;

  private Sessao sessao;

  @BeforeEach
  void setUp() {
    Pauta pauta = new Pauta("Reforma do estatuto", "Aprovar a nova redação");
    ReflectionTestUtils.setField(pauta, "id", PAUTA_ID);

    sessao = new Sessao(pauta, Duration.ofMinutes(1));
    ReflectionTestUtils.setField(sessao, "id", 1L);
  }

  @Test
  @DisplayName("POST deve retornar 201 com a sessão aberta quando o corpo não for enviado")
  void deveAbrirSessaoSemCorpo() throws Exception {
    when(sessaoService.create(eq(PAUTA_ID), isNull())).thenReturn(sessao);

    mockMvc.perform(post(URL))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.pautaId").value(PAUTA_ID))
        .andExpect(jsonPath("$.pautaTitulo").value("Reforma do estatuto"))
        .andExpect(jsonPath("$.inicio").exists())
        .andExpect(jsonPath("$.fim").exists());

    verify(sessaoService).create(PAUTA_ID, null);
  }

  @Test
  @DisplayName("POST deve repassar a duração informada ao service")
  void deveAbrirSessaoComDuracao() throws Exception {
    when(sessaoService.create(eq(PAUTA_ID), any(CriarSessaoRequest.class))).thenReturn(sessao);

    mockMvc.perform(post(URL)
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"duracaoEmMinutos\": 120}"))
        .andExpect(status().isCreated());

    verify(sessaoService).create(PAUTA_ID, new CriarSessaoRequest(120));
  }

  @ParameterizedTest
  @ValueSource(ints = { 0, -5 })
  @DisplayName("POST deve retornar 400 quando a duração não for positiva")
  void deveRejeitarDuracaoInvalida(int duracao) throws Exception {
    mockMvc.perform(post(URL)
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"duracaoEmMinutos\": " + duracao + "}"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors.duracaoEmMinutos").value("A duração deve ser maior que zero"));

    verify(sessaoService, never()).create(any(), any());
  }

  @Test
  @DisplayName("POST deve retornar 404 quando a pauta não existir")
  void deveRetornar404PautaInexistente() throws Exception {
    when(sessaoService.create(eq(PAUTA_ID), any()))
        .thenThrow(new RecursoNaoEncontradoException("Pauta não encontrada"));

    mockMvc.perform(post(URL))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value("Pauta não encontrada"));
  }

  @Test
  @DisplayName("POST deve retornar 409 quando a pauta já tiver sessão")
  void deveRetornar409SessaoDuplicada() throws Exception {
    when(sessaoService.create(eq(PAUTA_ID), any()))
        .thenThrow(new ConflitoException("Já existe uma sessão para esta pauta"));

    mockMvc.perform(post(URL))
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.message").value("Já existe uma sessão para esta pauta"));
  }

  @Test
  @DisplayName("GET deve retornar 200 com a sessão da pauta")
  void deveBuscarSessao() throws Exception {
    when(sessaoService.getByPautaId(PAUTA_ID)).thenReturn(sessao);

    mockMvc.perform(get(URL))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.pautaId").value(PAUTA_ID))
        .andExpect(jsonPath("$.pautaTitulo").value("Reforma do estatuto"));
  }

  @Test
  @DisplayName("GET deve retornar 404 quando a pauta não tiver sessão")
  void deveRetornar404SemSessao() throws Exception {
    when(sessaoService.getByPautaId(PAUTA_ID))
        .thenThrow(new RecursoNaoEncontradoException("Sessão não encontrada para esta pauta"));

    mockMvc.perform(get(URL))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value("Sessão não encontrada para esta pauta"));
  }
}
