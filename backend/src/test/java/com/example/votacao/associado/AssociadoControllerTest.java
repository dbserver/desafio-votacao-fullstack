package com.example.votacao.associado;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
import com.example.votacao.shared.ConflitoException;
import com.example.votacao.shared.RecursoNaoEncontradoException;

@WebMvcTest(AssociadoController.class)
class AssociadoControllerTest {

  private static final String URL = "/api/v1/associado";
  private static final String CPF = "52998224725";

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private AssociadoService associadoService;

  @Test
  @DisplayName("POST deve retornar 201 com o associado criado")
  void deveCriarAssociado() throws Exception {
    when(associadoService.create(any(CriarAssociadoRequest.class))).thenReturn(new Associado("Maria", CPF));

    mockMvc.perform(post(URL)
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"nome\": \"Maria\", \"cpf\": \"529.982.247-25\"}"))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.nome").value("Maria"))
        .andExpect(jsonPath("$.cpf").value(CPF));

    verify(associadoService).create(new CriarAssociadoRequest("Maria", "529.982.247-25"));
  }

  @Test
  @DisplayName("POST deve retornar 400 quando o nome estiver em branco")
  void deveRejeitarNomeEmBranco() throws Exception {
    mockMvc.perform(post(URL)
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"nome\": \"   \", \"cpf\": \"" + CPF + "\"}"))
        .andExpect(status().isBadRequest());

    verify(associadoService, never()).create(any());
  }

  @Test
  @DisplayName("POST deve retornar 400 quando o CPF for inválido")
  void deveRejeitarCpfInvalido() throws Exception {
    mockMvc.perform(post(URL)
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"nome\": \"Maria\", \"cpf\": \"12345678900\"}"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors.cpf").value("CPF inválido"));

    verify(associadoService, never()).create(any());
  }

  @Test
  @DisplayName("POST deve retornar 400 quando o corpo estiver vazio")
  void deveRejeitarCorpoVazio() throws Exception {
    mockMvc.perform(post(URL)
        .contentType(MediaType.APPLICATION_JSON)
        .content("{}"))
        .andExpect(status().isBadRequest());

    verify(associadoService, never()).create(any());
  }

  @Test
  @DisplayName("GET por CPF deve retornar 200 com o associado")
  void deveBuscarPorCpf() throws Exception {
    when(associadoService.getByCpf("529.982.247-25")).thenReturn(new Associado("Maria", CPF));

    mockMvc.perform(get(URL + "/cpf/529.982.247-25"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.nome").value("Maria"))
        .andExpect(jsonPath("$.cpf").value(CPF));
  }

  @Test
  @DisplayName("GET por CPF deve retornar 404 quando não houver associado")
  void deveRetornar404CpfInexistente() throws Exception {
    when(associadoService.getByCpf(CPF)).thenThrow(new RecursoNaoEncontradoException("Associado não encontrado"));

    mockMvc.perform(get(URL + "/cpf/" + CPF))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value("Associado não encontrado"));
  }

  @Test
  @DisplayName("POST deve retornar 409 quando o CPF já estiver cadastrado")
  void deveRetornar409CpfDuplicado() throws Exception {
    when(associadoService.create(any(CriarAssociadoRequest.class)))
        .thenThrow(new ConflitoException("Já existe um associado com este CPF"));

    mockMvc.perform(post(URL)
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"nome\": \"Maria\", \"cpf\": \"" + CPF + "\"}"))
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.message").value("Já existe um associado com este CPF"));
  }
}
