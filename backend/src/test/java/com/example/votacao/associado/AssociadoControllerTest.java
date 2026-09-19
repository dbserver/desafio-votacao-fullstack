package com.example.votacao.associado;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.votacao.associado.dto.CriarAssociadoRequest;

@WebMvcTest(AssociadoController.class)
class AssociadoControllerTest {

  private static final String URL = "/api/v1/associado";

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private AssociadoService associadoService;

  @Test
  @DisplayName("POST deve retornar 201 com o associado criado")
  void deveCriarAssociado() throws Exception {
    when(associadoService.create(any(CriarAssociadoRequest.class))).thenReturn(new Associado("Maria"));

    mockMvc.perform(post(URL)
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"nome\": \"Maria\"}"))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.nome").value("Maria"));

    verify(associadoService).create(new CriarAssociadoRequest("Maria"));
  }

  @Test
  @DisplayName("POST deve retornar 400 quando o nome estiver em branco")
  void deveRejeitarNomeEmBranco() throws Exception {
    mockMvc.perform(post(URL)
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"nome\": \"   \"}"))
        .andExpect(status().isBadRequest());

    verify(associadoService, never()).create(any());
  }

  @Test
  @DisplayName("POST deve retornar 400 quando o nome não for enviado")
  void deveRejeitarNomeAusente() throws Exception {
    mockMvc.perform(post(URL)
        .contentType(MediaType.APPLICATION_JSON)
        .content("{}"))
        .andExpect(status().isBadRequest());

    verify(associadoService, never()).create(any());
  }
}
