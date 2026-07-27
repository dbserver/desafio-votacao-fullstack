package com.dbchal.voting.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.dbchal.voting.domain.enums.OpcaoVoto;
import com.dbchal.voting.dto.voto.CreateVotoRequest;
import com.dbchal.voting.dto.voto.VotoResponse;
import com.dbchal.voting.exception.voto.DuplicateVoteException;
import com.dbchal.voting.service.VotoService;

@WebMvcTest(VotoController.class)
class VotoControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private VotoService votoService;

	@Test
	void shouldReturnCreatedWhenVoteIsRegistered() throws Exception {
		VotoResponse response = new VotoResponse(1L,
												 1L,
												 1L,
												 1L,
												 OpcaoVoto.SIM,
												 Instant.parse("2026-07-25T20:00:00Z"));

		when(votoService.votar(eq(1L), any(CreateVotoRequest.class))).thenReturn(response);

		mockMvc.perform(post("/api/v1/votos/{pautaId}", 1L).contentType(MediaType.APPLICATION_JSON).content("""
				{
				  "associadoId": 1,
				  "opcao": "SIM"
				}
				""")).andExpect(status().isCreated())
					 .andExpect(jsonPath("$.id").value(1))
					 .andExpect(jsonPath("$.associadoId").value(1))
					 .andExpect(jsonPath("$.opcao").value("SIM"));
	}

	@Test
	void shouldReturnBadRequestWhenRequestIsInvalid() throws Exception {
		mockMvc.perform(post("/api/v1/votos/{pautaId}", 1L).contentType(MediaType.APPLICATION_JSON).content("""
				{
				  "associadoId": 0
				}
				""")).andExpect(status().isBadRequest());

		verifyNoInteractions(votoService);
	}

	@Test
	void shouldReturnConflictWhenVoteIsDuplicated() throws Exception {
		when(votoService.votar(eq(1L), any(CreateVotoRequest.class))).thenThrow(new DuplicateVoteException());

		mockMvc.perform(post("/api/v1/votos/{pautaId}", 1L).contentType(MediaType.APPLICATION_JSON).content("""
				{
				  "associadoId": 1,
				  "opcao": "SIM"
				}
				""")).andExpect(status().isConflict());
	}
}