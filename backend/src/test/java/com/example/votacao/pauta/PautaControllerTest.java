package com.example.votacao.pauta;

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

import com.example.votacao.pauta.dto.CriarPautaRequest;

@WebMvcTest(PautaController.class)
class PautaControllerTest {

  private static final String URL = "/api/v1/pauta";

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private PautaService pautaService;

  @Test
  @DisplayName("POST deve retornar 201 com a pauta criada")
  void deveCriarPauta() throws Exception {
    when(pautaService.create(any(CriarPautaRequest.class)))
        .thenReturn(new Pauta("Reforma do estatuto", "Aprovar a nova redação do estatuto"));

    mockMvc.perform(post(URL)
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"titulo\": \"Reforma do estatuto\", \"descricao\": \"Aprovar a nova redação do estatuto\"}"))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.titulo").value("Reforma do estatuto"))
        .andExpect(jsonPath("$.descricao").value("Aprovar a nova redação do estatuto"))
        .andExpect(jsonPath("$.criadoEm").exists());

    verify(pautaService).create(new CriarPautaRequest("Reforma do estatuto", "Aprovar a nova redação do estatuto"));
  }

  @Test
  @DisplayName("POST deve retornar 400 quando o título estiver em branco")
  void deveRejeitarTituloEmBranco() throws Exception {
    mockMvc.perform(post(URL)
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"titulo\": \"   \", \"descricao\": \"Aprovar a nova redação do estatuto\"}"))
        .andExpect(status().isBadRequest());

    verify(pautaService, never()).create(any());
  }

  @Test
  @DisplayName("POST deve retornar 400 quando a descrição não for enviada")
  void deveRejeitarDescricaoAusente() throws Exception {
    mockMvc.perform(post(URL)
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"titulo\": \"Reforma do estatuto\"}"))
        .andExpect(status().isBadRequest());

    verify(pautaService, never()).create(any());
  }

  @Test
  @DisplayName("POST deve retornar 400 quando o corpo estiver vazio")
  void deveRejeitarCorpoVazio() throws Exception {
    mockMvc.perform(post(URL)
        .contentType(MediaType.APPLICATION_JSON)
        .content("{}"))
        .andExpect(status().isBadRequest());

    verify(pautaService, never()).create(any());
  }
}
