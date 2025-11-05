package com.db.votacaobackend.agenda.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.db.votacaobackend.agenda.dto.AgendaDTO;
import com.db.votacaobackend.agenda.model.Agenda;
import com.db.votacaobackend.agenda.repository.AgendaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("AgendaController - Testing routes and business rules of the agenda")
class AgendaControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private AgendaRepository agendaRepository;
  @Autowired
  private ObjectMapper objectMapper;

  @AfterEach
  void clean() {
    agendaRepository.deleteAll();
  }

  @Test
  @DisplayName("Test create agenda success")
  void testCreateAgendaSuccess() throws Exception {
    AgendaDTO requestDto = new AgendaDTO(null, "Realização de testes");

    mockMvc.perform(post("/v1/agenda")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestDto)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").exists())
        .andExpect(jsonPath("$.name").value("Realização de testes"));
  }

  @Test
  @DisplayName("Test bad request exception in create agenda")
  void testCreateAgendaWithoutName() throws Exception {
    AgendaDTO invalidAgenda = new AgendaDTO(null, null);

    mockMvc.perform(post("/v1/agenda")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(invalidAgenda)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("O nome da pauta é obrigatório"));
  }

  @Test
  @DisplayName("Test list agenda")
  void testListAllAgendasSuccess() throws Exception {

    Agenda agenda1 = new Agenda(null, "Reunião de planejamento");
    Agenda agenda2 = new Agenda(null, "Votos para orçamento");

    agendaRepository.save(agenda1);
    agendaRepository.save(agenda2);

    mockMvc.perform(get("/v1/agenda")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].name").value("Reunião de planejamento"))
        .andExpect(jsonPath("$[1].name").value("Votos para orçamento"));
  }
}
