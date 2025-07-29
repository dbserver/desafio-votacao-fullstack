package com.nunesd66.pautas_api.voto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nunesd66.pautas_api.voto.controller.VotoControllerImpl;
import com.nunesd66.pautas_api.voto.exception.VotoException;
import com.nunesd66.pautas_api.voto.model.Voto;
import com.nunesd66.pautas_api.voto.service.VotoService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = VotoControllerImpl.class)
class VotoControllerImplTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private VotoService votoService;

    @Test
    void postVotar_success_NoContent() throws Exception {
        Voto voto = new Voto();
        voto.setCpfAssociado("123");
        doNothing().when(votoService).votar(any(Voto.class));

        mockMvc.perform(post("/voto/votar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(voto))
                )
                .andExpect(status().isNoContent());
    }

    @Test
    void postVotar_error_returnsBadRequest() throws Exception {
        Voto voto = new Voto();
        voto.setCpfAssociado("456");

        doThrow(new VotoException("erro voto"))
                .when(votoService).votar(any(Voto.class));

        mockMvc.perform(post("/voto/votar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(voto))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.error").value("erro voto"));
    }
}
