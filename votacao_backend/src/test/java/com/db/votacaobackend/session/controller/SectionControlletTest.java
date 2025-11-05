package com.db.votacaobackend.session.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.db.votacaobackend.agenda.model.Agenda;
import com.db.votacaobackend.agenda.service.AgendaService;
import com.db.votacaobackend.section.dto.CreateSectionDTO;
import com.db.votacaobackend.section.repository.SectionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@TestInstance(Lifecycle.PER_CLASS)
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("SectionController - Testing routes and business rules of the section")
public class SectionControlletTest {

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private SectionRepository sectionRepository;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private AgendaService agendaService;

  @AfterAll
  void clean() {
    sectionRepository.deleteAll();
  }

  @Test
  @DisplayName("Test create session when not found agenda id")
  void testCreateSessionWhenAgendaDoesNotExist() throws Exception {

    CreateSectionDTO requestDto = new CreateSectionDTO(5, 8L);

    mockMvc.perform(post("/v1/section")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestDto)))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value("Pauta não encontrada!"));

  }


  @Test
  @DisplayName("Test create section success")
  void testCreateSessionSuccess() throws Exception {

    Agenda newAgenda = new Agenda(null, "Pauta de testes");
    Agenda newAgenda2 = new Agenda(null, "Pauta de testes 2");
    Agenda createdAgenda = agendaService.createAgenda(newAgenda);
    agendaService.createAgenda(newAgenda2);

    CreateSectionDTO requestDto = new CreateSectionDTO(1, createdAgenda.getId());

    mockMvc.perform(post("/v1/section")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestDto)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").exists())
        .andExpect(jsonPath("$.agendaId.id").value(createdAgenda.getId()));
  }

  @Test
  @DisplayName("Test list all section")
  void testeListAllSessionSuccess() throws Exception {

    mockMvc.perform(get("/v1/section")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].duration").value(1))
        .andExpect(jsonPath("$[0].agendaId.name").value("Pauta de testes"));
  }


  @Test
  @DisplayName("Test availiable agendas to create section ")
  void testAvailiableAgendaToCreateSection() throws Exception {

    mockMvc.perform(get("/v1/section/availiable-agendas")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].name").value("Pauta de testes 2"));

  }

  @Test
  @DisplayName("Test list all availiable section to vote")
  void testeListAllAvailiableSessiontoVote() throws Exception {

    Thread.sleep(60 * 1000);

    mockMvc.perform(get("/v1/section/open")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(0)));
  }

}

