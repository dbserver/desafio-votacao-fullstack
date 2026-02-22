package dev.rodrigo.desafiovotacao.controller;

import dev.rodrigo.desafiovotacao.dto.PautaResponseDto;
import dev.rodrigo.desafiovotacao.entity.Pauta;
import dev.rodrigo.desafiovotacao.mappers.PautaMapper;
import dev.rodrigo.desafiovotacao.service.PautaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PautaController.class)
class PautaControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockitoBean private PautaService service;

  @MockitoBean private PautaMapper mapper;

  @Test
  void deveRetornarPautaPorId() throws Exception {

    Pauta pauta = new Pauta();
    pauta.setId(1L);

    PautaResponseDto dto = new PautaResponseDto();
    dto.setId(1L);

    when(service.buscarPautaPorId(1L)).thenReturn(pauta);
    when(mapper.toPautaResponseDto(pauta)).thenReturn(dto);

    mockMvc.perform(get("/api/1")).andExpect(status().isOk()).andExpect(jsonPath("$.id").value(1));
  }

  @Test
  void deveCriarPautaERetornar201() throws Exception {

    String json =
        """
            {
              "titulo": "Nova pauta",
              "descricao": "Descrição"
            }
        """;

    mockMvc
        .perform(post("/api").contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().isCreated());
  }

  @Test
  void deveRetornarTodasAsPautas() throws Exception {
    List<Pauta> pautaList = new ArrayList<>();

    Pauta pauta = new Pauta();
    pauta.setId(1L);
    pautaList.add(pauta);

    List<PautaResponseDto> responseDtoList = new ArrayList<>();
    PautaResponseDto dto = new PautaResponseDto();
    dto.setId(1L);
    responseDtoList.add(dto);

    when(service.buscarPautas()).thenReturn(pautaList);
    when(mapper.toPautaResponseList(pautaList)).thenReturn(responseDtoList);

    mockMvc.perform(get("/api")).andExpect(status().isOk());
  }
}
