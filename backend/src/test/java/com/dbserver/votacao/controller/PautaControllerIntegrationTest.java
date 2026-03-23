package com.dbserver.votacao.controller;

import com.dbserver.votacao.dto.PautaRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PautaControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deveCriarPautaComSucesso() throws Exception {
        PautaRequest request = new PautaRequest("Pauta Integração", "Teste de integração");

        mockMvc.perform(post("/api/v1/pautas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.titulo").value("Pauta Integração"))
                .andExpect(jsonPath("$.id").isNumber());
    }

    @Test
    void deveListarPautas() throws Exception {
        PautaRequest request = new PautaRequest("Pauta Lista", "Teste listagem");
        mockMvc.perform(post("/api/v1/pautas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        mockMvc.perform(get("/api/v1/pautas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void deveRetornar400QuandoTituloVazio() throws Exception {
        PautaRequest request = new PautaRequest("", null);

        mockMvc.perform(post("/api/v1/pautas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar404QuandoPautaNaoExiste() throws Exception {
        mockMvc.perform(get("/api/v1/pautas/99999"))
                .andExpect(status().isNotFound());
    }
}
