package com.db.votacaobackend.vote.controller;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.db.votacaobackend.agenda.model.Agenda;
import com.db.votacaobackend.agenda.service.AgendaService;
import com.db.votacaobackend.section.dto.CreateSectionDTO;
import com.db.votacaobackend.section.dto.SectionDetailsDTO;
import com.db.votacaobackend.section.service.SectionService;
import com.db.votacaobackend.vote.dto.VoteCreateDTO;
import com.db.votacaobackend.vote.model.VoteOption;
import com.db.votacaobackend.vote.repository.VoteRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
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

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("VoteController - Testing routes and business rules of the vote")
public class VoteControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private VoteRepository voteRepository;

  @Autowired
  private SectionService sectionService;

  @Autowired
  private AgendaService agendaService;


  @AfterEach
  void clean() {
    voteRepository.deleteAll();
  }

  @Test
  @DisplayName("Test create vote when not found section id")
  void testCreateVoteWhenSectionDoesNotExist() throws Exception {

    VoteCreateDTO vote = new VoteCreateDTO(
        1L,
        VoteOption.SIM,
        "12345678901"
    );

    mockMvc.perform(post("/v1/vote")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(vote)))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value("Sessão não encontrada!"));

  }

}
