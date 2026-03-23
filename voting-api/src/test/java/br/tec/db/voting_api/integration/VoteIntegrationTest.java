package br.tec.db.voting_api.integration;

import br.tec.db.voting_api.external.cpf.CpfClient;
import br.tec.db.voting_api.external.cpf.CpfResponse;
import br.tec.db.voting_api.external.cpf.CpfStatus;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
public class VoteIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldExecuteCompleteVotingFlow() throws Exception {

        String AgendaJson = """
        {
          "title": "Pauta teste",
          "description": "Descrição teste"
        }
        """;

        MvcResult agendaResult = mockMvc.perform(post("/api/v1/agenda")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(AgendaJson))
                .andExpect(status().isOk())
                .andReturn();


        mockMvc.perform(post("/api/v1/session/1/open")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk());

        String voteJson = """
        {
          "agendaId": 1,
          "associatedId": "12345",
          "vote": "YES"
        }
        """;

        mockMvc.perform(post("/api/v1/vote/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(voteJson))
                .andExpect(status().isOk());


        mockMvc.perform(get("/api/v1/vote-result/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalYes").value(1))
                .andExpect(jsonPath("$.totalNo").value(0));
    }

}
