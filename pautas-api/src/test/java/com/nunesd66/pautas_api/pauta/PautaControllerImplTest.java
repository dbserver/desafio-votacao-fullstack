package com.nunesd66.pautas_api.pauta;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nunesd66.pautas_api.pauta.controller.PautaControllerImpl;
import com.nunesd66.pautas_api.pauta.dto.PautaCreateRequest;
import com.nunesd66.pautas_api.pauta.dto.PautaResponse;
import com.nunesd66.pautas_api.pauta.exception.PautaException;
import com.nunesd66.pautas_api.pauta.service.PautaService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PautaControllerImpl.class)
@Import(PautaControllerImplTest.Config.class)
class PautaControllerImplTest {

    @TestConfiguration
    static class Config {
        @Bean
        public PautaService pautaService() {
            return Mockito.mock(PautaService.class);
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PautaService pautaService;

    @Test
    void postSave_success() throws Exception {
        PautaCreateRequest req = new PautaCreateRequest("T","D", null);
        PautaResponse resp = new PautaResponse(1L, "T","D", null);
        Mockito.when(pautaService.save(req)).thenReturn(resp);

        mockMvc.perform(post("/pauta")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(req))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.titulo").value("T"))
                .andExpect(jsonPath("$.error").isEmpty());
    }

    @Test
    void postSave_validationError() throws Exception {
        PautaCreateRequest req = new PautaCreateRequest("","D", null);
        Mockito.when(pautaService.save(req))
                .thenThrow(new PautaException("err"));

        mockMvc.perform(post("/pauta")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(req))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.error").value("err"));
    }

    @Test
    void getAll_success() throws Exception {
        PautaResponse r1 = new PautaResponse(1L, "A","D1", null);
        PautaResponse r2 = new PautaResponse(2L, "B","D2", null);
        Mockito.when(pautaService.getAll()).thenReturn(List.of(r1, r2));

        mockMvc.perform(get("/pauta"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.error").isEmpty());
    }

    @Test
    void getLastRecord_success() throws Exception {
        PautaResponse last = new PautaResponse(5L, "X","Y", null);
        Mockito.when(pautaService.getLastRecord()).thenReturn(last);

        mockMvc.perform(get("/pauta/last-record"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(5))
                .andExpect(jsonPath("$.error").isEmpty());
    }
}
