package dev.rodrigo.desafiovotacao.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import dev.rodrigo.desafiovotacao.dto.ResultadoVotacaoDto;
import dev.rodrigo.desafiovotacao.dto.VotoRequestDto;
import dev.rodrigo.desafiovotacao.dto.VotoResponseDto;
import dev.rodrigo.desafiovotacao.entity.Voto;
import dev.rodrigo.desafiovotacao.enums.ResultadoVotacao;
import dev.rodrigo.desafiovotacao.enums.TipoVoto;
import dev.rodrigo.desafiovotacao.mappers.VotoMapper;
import dev.rodrigo.desafiovotacao.service.VotoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

@WebMvcTest(VotoController.class)
class VotoControllerTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @MockitoBean private VotoService service;

  @MockitoBean private VotoMapper mapper;

  @Test
  void deveRegistrarVotoERetornar201() throws Exception {

    Long sessaoId = 1L;

    VotoRequestDto request = new VotoRequestDto();
    request.setCpf("12345678901");
    request.setVoto(TipoVoto.SIM);

    Voto voto = new Voto();
    voto.setId(10L);

    VotoResponseDto responseDto = new VotoResponseDto();
    responseDto.setId(10L);
    responseDto.setCpf("12345678901");
    responseDto.setVoto(TipoVoto.SIM);

    when(service.votar(sessaoId, request)).thenReturn(voto);
    when(mapper.toVotoResponseDto(voto)).thenReturn(responseDto);

    mockMvc
        .perform(
            post("/api/votos/sessao/{sessaoId}", sessaoId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(10L))
        .andExpect(jsonPath("$.cpf").value("12345678901"))
        .andExpect(jsonPath("$.voto").value("SIM"));
  }

  @Test
  void deveRetornarResultadoDaSessao() throws Exception {

    Long sessaoId = 1L;

    ResultadoVotacaoDto resultado = new ResultadoVotacaoDto();
    resultado.setVotosSim(5L);
    resultado.setVotosNao(3L);
    resultado.setResultado(ResultadoVotacao.APROVADO);

    when(service.contabilizar(sessaoId)).thenReturn(resultado);

    mockMvc
        .perform(get("/api/votos/sessao/{sessaoId}/resultado", sessaoId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.votosSim").value(5))
        .andExpect(jsonPath("$.votosNao").value(3))
        .andExpect(jsonPath("$.resultado").value("APROVADO"));
  }
}
