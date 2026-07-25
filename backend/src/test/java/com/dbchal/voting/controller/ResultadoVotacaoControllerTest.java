package com.dbchal.voting.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.dbchal.voting.domain.enums.ResultadoVotacaoStatus;
import com.dbchal.voting.dto.voto.ResultadoVotacaoResponse;
import com.dbchal.voting.exception.sessao.SessaoNotFoundException;
import com.dbchal.voting.service.ResultadoVotacaoService;

@WebMvcTest(ResultadoVotacaoController.class)
class ResultadoVotacaoControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private ResultadoVotacaoService resultadoVotacaoService;

	@Test
	void shouldReturnVotingResult() throws Exception {
		ResultadoVotacaoResponse response =
				new ResultadoVotacaoResponse(
						1L,
						1L,
						75L,
						20L,
						95L,
						false,
						ResultadoVotacaoStatus.APROVADA
				);

		when(resultadoVotacaoService.getResult(1L))
				.thenReturn(response);

		mockMvc.perform(get("/api/v1/resultados/{pautaId}", 1L))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.pautaId").value(1))
			.andExpect(jsonPath("$.sessaoId").value(1))
			.andExpect(jsonPath("$.votosSim").value(75))
			.andExpect(jsonPath("$.votosNao").value(20))
			.andExpect(jsonPath("$.totalVotos").value(95))
			.andExpect(jsonPath("$.sessaoAberta").value(false))
			.andExpect(jsonPath("$.resultado").value("APROVADA"));
	}
	
	@Test
	void shouldReturnNotFoundWhenSessionDoesNotExist() throws Exception {
		Long pautaId = 999L;

		when(resultadoVotacaoService.getResult(pautaId))
				.thenThrow(new SessaoNotFoundException(pautaId));

		mockMvc.perform(get("/api/v1/resultados/{pautaId}", pautaId))
			.andExpect(status().isNotFound());
	}
}