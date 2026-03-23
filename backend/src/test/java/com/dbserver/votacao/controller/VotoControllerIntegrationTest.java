package com.dbserver.votacao.controller;

import com.dbserver.votacao.dto.PautaRequest;
import com.dbserver.votacao.dto.SessaoVotacaoRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class VotoControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Long criarPauta(String titulo) throws Exception {
        PautaRequest request = new PautaRequest(titulo, "Descrição");
        MvcResult result = mockMvc.perform(post("/api/v1/pautas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        return objectMapper.readTree(result.getResponse().getContentAsString()).get("id").asLong();
    }

    @Test
    void deveCriarSessaoDeVotacao() throws Exception {
        Long pautaId = criarPauta("Pauta para Sessão");

        SessaoVotacaoRequest sessaoRequest = new SessaoVotacaoRequest(pautaId, 5);
        mockMvc.perform(post("/api/v1/sessoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sessaoRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.pautaId").value(pautaId))
                .andExpect(jsonPath("$.aberta").value(true));
    }

    @Test
    void deveRetornar404QuandoResultadoSemSessao() throws Exception {
        mockMvc.perform(get("/api/v1/pautas/99999/votos/resultado"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveRetornar400QuandoSessaoDuplicada() throws Exception {
        Long pautaId = criarPauta("Pauta Duplicar Sessão");

        SessaoVotacaoRequest sessaoRequest = new SessaoVotacaoRequest(pautaId, 5);
        mockMvc.perform(post("/api/v1/sessoes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sessaoRequest)));

        mockMvc.perform(post("/api/v1/sessoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sessaoRequest)))
                .andExpect(status().isBadRequest());
    }
}
