package com.desafio.votacao_api.integration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class GlobalExceptionHandlerIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void shouldReturnValidationErrorWithFieldNameWhenRequiredBodyFieldIsMissing() throws Exception {
        String payload = """
                {
                  "description": "Descricao de teste"
                }
                """;

        mockMvc.perform(post("/api/v1/pautas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation Error"))
                .andExpect(jsonPath("$.message", containsString("name:")))
                .andExpect(jsonPath("$.path").value("/api/v1/pautas"));
    }

    @Test
    void shouldReturnSpecificMessageWhenRequestBodyIsMissing() throws Exception {
        mockMvc.perform(post("/api/v1/pautas")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Malformed Request Body"))
                .andExpect(jsonPath("$.message").value("Request body is required."))
                .andExpect(jsonPath("$.path").value("/api/v1/pautas"));
    }

    @Test
    void shouldReturnSpecificMessageWhenEnumValueIsInvalid() throws Exception {
        String payload = """
                {
                  "sessaoVotacaoId": "%s",
                  "cpf": "12345678901",
                  "vote": "TALVEZ"
                }
                """.formatted(UUID.randomUUID());

        mockMvc.perform(post("/api/v1/votos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Malformed Request Body"))
                .andExpect(jsonPath("$.message", containsString("Invalid value for field")))
                .andExpect(jsonPath("$.path").value("/api/v1/votos"));
    }

    @Test
    void shouldReturnSpecificMessageWhenPathVariableTypeIsInvalid() throws Exception {
        mockMvc.perform(get("/api/v1/pautas/nao-e-uuid"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Invalid Request Parameter"))
                .andExpect(jsonPath("$.message").value("Invalid value for parameter 'id'."))
                .andExpect(jsonPath("$.path").value("/api/v1/pautas/nao-e-uuid"));
    }

    @Test
    void shouldReturnSpecificMessageWhenQueryParameterIsMissing() throws Exception {
        mockMvc.perform(get("/api/v1/test-params"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Missing Request Parameter"))
                .andExpect(jsonPath("$.message").value("Required parameter 'required' is missing."))
                .andExpect(jsonPath("$.path").value("/api/v1/test-params"));
    }
}


