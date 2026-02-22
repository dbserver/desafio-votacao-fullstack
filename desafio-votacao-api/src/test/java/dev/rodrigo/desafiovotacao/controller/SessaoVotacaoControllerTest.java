package dev.rodrigo.desafiovotacao.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import dev.rodrigo.desafiovotacao.dto.SessaoRequestDto;
import dev.rodrigo.desafiovotacao.dto.SessaoResponseDto;
import dev.rodrigo.desafiovotacao.entity.SessaoVotacao;
import dev.rodrigo.desafiovotacao.mappers.SessaoVotacaoMapper;
import dev.rodrigo.desafiovotacao.service.SessaoVotacaoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

@WebMvcTest(SessaoVotacaoController.class)
class SessaoVotacaoControllerTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @MockitoBean private SessaoVotacaoService service;

  @MockitoBean private SessaoVotacaoMapper mapper;

  @Test
  void deveAbrirSessaoERetornar201() throws Exception {

    Long pautaId = 1L;

    SessaoRequestDto request = new SessaoRequestDto();
    request.setDuracaoMinutos(10L);

    SessaoVotacao sessao = new SessaoVotacao();
    sessao.setId(100L);

    SessaoResponseDto responseDto = new SessaoResponseDto();
    responseDto.setId(100L);

    when(service.abrirSessao(pautaId, request)).thenReturn(sessao);
    when(mapper.toSessaoResponseDto(sessao)).thenReturn(responseDto);

    mockMvc
        .perform(
            post("/api/sessoes/pauta/{pautaId}", pautaId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(100L));
  }

  @Test
  void deveBuscarSessaoPorId() throws Exception {

    Long sessaoId = 1L;

    SessaoVotacao sessao = new SessaoVotacao();
    sessao.setId(sessaoId);

    SessaoResponseDto responseDto = new SessaoResponseDto();
    responseDto.setId(sessaoId);

    when(service.buscarPorId(sessaoId)).thenReturn(sessao);
    when(mapper.toSessaoResponseDto(sessao)).thenReturn(responseDto);

    mockMvc
        .perform(get("/api/sessoes/{id}", sessaoId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(sessaoId));
  }

  @Test
  void deveEncerrarSessao() throws Exception {

    Long sessaoId = 1L;

    doNothing().when(service).encerrarSessao(sessaoId);

    mockMvc
        .perform(post("/api/sessoes/sessao/encerrar/{sessaoId}", sessaoId))
        .andExpect(status().isOk());
  }

  @Test
  void deveAbrirSessaoSemTempoERetornar201() throws Exception {

    Long pautaId = 1L;

    SessaoRequestDto request = new SessaoRequestDto();

    SessaoVotacao sessao = new SessaoVotacao();
    sessao.setId(100L);

    SessaoResponseDto responseDto = new SessaoResponseDto();
    responseDto.setId(100L);

    when(service.abrirSessao(pautaId, request)).thenReturn(sessao);
    when(mapper.toSessaoResponseDto(sessao)).thenReturn(responseDto);

    mockMvc
        .perform(
            post("/api/sessoes/pauta/{pautaId}", pautaId).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(100L));
  }
}
