package com.dbchal.voting.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.dbchal.voting.domain.entity.SessaoVotacao;
import com.dbchal.voting.domain.enums.ResultadoVotacaoStatus;
import com.dbchal.voting.dto.voto.ResultadoVotacaoResponse;
import com.dbchal.voting.dto.voto.ResultadoVotosProjection;
import com.dbchal.voting.repository.VotoRepository;

@ExtendWith(MockitoExtension.class)
class ResultadoVotacaoServiceTest {

	@Mock
	private VotoRepository votoRepository;

	@Mock
	private SessaoVotacaoService sessaoVotacaoService;

	@InjectMocks
	private ResultadoVotacaoService resultadoVotacaoService;

	@Test
	void shouldReturnEmAndamentoResult() {
		Long pautaId = 1L;
		Long sessaoId = 1L;

		SessaoVotacao sessao = mock(SessaoVotacao.class);

		ResultadoVotosProjection contagem = mock(ResultadoVotosProjection.class);

		when(sessaoVotacaoService.findEntityByPauta(pautaId)).thenReturn(sessao);

		when(sessao.getId()).thenReturn(sessaoId);
		when(sessao.isOpen()).thenReturn(true);

		when(votoRepository.countBySessaoId(sessaoId)).thenReturn(contagem);

		when(contagem.getVotosSim()).thenReturn(90L);
		when(contagem.getVotosNao()).thenReturn(2L);

		ResultadoVotacaoResponse response = resultadoVotacaoService.getResult(pautaId);

		assertEquals(90L, response.votosSim());
		assertEquals(2L, response.votosNao());
		assertEquals(92L, response.totalVotos());
		assertEquals(ResultadoVotacaoStatus.EM_ANDAMENTO, response.resultado());
	}

	@Test
	void shouldReturnAprovadaResult() {
		Long pautaId = 1L;
		Long sessaoId = 1L;

		SessaoVotacao sessao = mock(SessaoVotacao.class);

		ResultadoVotosProjection contagem = mock(ResultadoVotosProjection.class);

		when(sessaoVotacaoService.findEntityByPauta(pautaId)).thenReturn(sessao);

		when(sessao.getId()).thenReturn(sessaoId);
		when(sessao.isOpen()).thenReturn(false);

		when(votoRepository.countBySessaoId(sessaoId)).thenReturn(contagem);

		when(contagem.getVotosSim()).thenReturn(5L);
		when(contagem.getVotosNao()).thenReturn(2L);

		ResultadoVotacaoResponse response = resultadoVotacaoService.getResult(pautaId);

		assertEquals(5L, response.votosSim());
		assertEquals(2L, response.votosNao());
		assertEquals(7L, response.totalVotos());
		assertEquals(ResultadoVotacaoStatus.APROVADA, response.resultado());
	}

	@Test
	void shouldReturnReprovadaResult() {
		Long pautaId = 1L;
		Long sessaoId = 1L;

		SessaoVotacao sessao = mock(SessaoVotacao.class);

		ResultadoVotosProjection contagem = mock(ResultadoVotosProjection.class);

		when(sessaoVotacaoService.findEntityByPauta(pautaId)).thenReturn(sessao);

		when(sessao.getId()).thenReturn(sessaoId);
		when(sessao.isOpen()).thenReturn(false);

		when(votoRepository.countBySessaoId(sessaoId)).thenReturn(contagem);

		when(contagem.getVotosSim()).thenReturn(2L);
		when(contagem.getVotosNao()).thenReturn(5L);

		ResultadoVotacaoResponse response = resultadoVotacaoService.getResult(pautaId);

		assertEquals(2L, response.votosSim());
		assertEquals(5L, response.votosNao());
		assertEquals(7L, response.totalVotos());
		assertEquals(ResultadoVotacaoStatus.REPROVADA, response.resultado());
	}

	@Test
	void shouldReturnEmpateResult() {
		Long pautaId = 1L;
		Long sessaoId = 1L;

		SessaoVotacao sessao = mock(SessaoVotacao.class);

		ResultadoVotosProjection contagem = mock(ResultadoVotosProjection.class);

		when(sessaoVotacaoService.findEntityByPauta(pautaId)).thenReturn(sessao);

		when(sessao.getId()).thenReturn(sessaoId);
		when(sessao.isOpen()).thenReturn(false);

		when(votoRepository.countBySessaoId(sessaoId)).thenReturn(contagem);

		when(contagem.getVotosSim()).thenReturn(5L);
		when(contagem.getVotosNao()).thenReturn(5L);

		ResultadoVotacaoResponse response = resultadoVotacaoService.getResult(pautaId);

		assertEquals(5L, response.votosSim());
		assertEquals(5L, response.votosNao());
		assertEquals(10L, response.totalVotos());
		assertEquals(ResultadoVotacaoStatus.EMPATE, response.resultado());
	}

	@Test
	void shouldReturnSemVotosResult() {
		Long pautaId = 1L;
		Long sessaoId = 1L;

		SessaoVotacao sessao = mock(SessaoVotacao.class);

		ResultadoVotosProjection contagem = mock(ResultadoVotosProjection.class);

		when(sessaoVotacaoService.findEntityByPauta(pautaId)).thenReturn(sessao);

		when(sessao.getId()).thenReturn(sessaoId);
		when(sessao.isOpen()).thenReturn(false);

		when(votoRepository.countBySessaoId(sessaoId)).thenReturn(contagem);

		when(contagem.getVotosSim()).thenReturn(0L);
		when(contagem.getVotosNao()).thenReturn(0L);

		ResultadoVotacaoResponse response = resultadoVotacaoService.getResult(pautaId);

		assertEquals(0L, response.votosSim());
		assertEquals(0L, response.votosNao());
		assertEquals(0L, response.totalVotos());
		assertEquals(ResultadoVotacaoStatus.SEM_VOTOS, response.resultado());
	}
}