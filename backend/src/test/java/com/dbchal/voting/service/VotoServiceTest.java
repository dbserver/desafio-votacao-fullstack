package com.dbchal.voting.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.util.ReflectionTestUtils;

import com.dbchal.voting.domain.entity.Associado;
import com.dbchal.voting.domain.entity.Pauta;
import com.dbchal.voting.domain.entity.SessaoVotacao;
import com.dbchal.voting.domain.entity.Voto;
import com.dbchal.voting.domain.enums.CpfValidationStatus;
import com.dbchal.voting.domain.enums.OpcaoVoto;
import com.dbchal.voting.dto.voto.CreateVotoRequest;
import com.dbchal.voting.dto.voto.VotoResponse;
import com.dbchal.voting.exception.CreateEntityException;
import com.dbchal.voting.exception.associado.InvalidCpfException;
import com.dbchal.voting.exception.sessao.ExpiredSessaoException;
import com.dbchal.voting.exception.voto.AssociadoUnableToVoteException;
import com.dbchal.voting.exception.voto.DuplicateVoteException;
import com.dbchal.voting.facade.CpfValidationFacade;
import com.dbchal.voting.repository.VotoRepository;

@ExtendWith(MockitoExtension.class)
public class VotoServiceTest {

	@Mock
	private VotoRepository votoRepository;

	@Mock
	private SessaoVotacaoService sessaoVotacaoService;

	@Mock
	private AssociadoService associadoService;
	
	@Mock
	private CpfValidationFacade cpfValidationFacade;

	@InjectMocks
	private VotoService votoService;

	@Test
	void shouldRegisterVote() {
		Long pautaId = 1L;
		Long sessaoId = 1L;
		Long associadoId = 1L;
		String cpf = "52998224725";

		CreateVotoRequest request = new CreateVotoRequest(associadoId, OpcaoVoto.SIM);

		Pauta pauta = mock(Pauta.class);
		SessaoVotacao sessao = mock(SessaoVotacao.class);
		Associado associado = mock(Associado.class);

		when(sessaoVotacaoService.findEntityByPauta(pautaId)).thenReturn(sessao);

		when(sessao.getId()).thenReturn(sessaoId);
		when(sessao.getPauta()).thenReturn(pauta);
		when(sessao.isOpen()).thenReturn(true);

		when(pauta.getId()).thenReturn(pautaId);

		when(associadoService.findEntityById(associadoId)).thenReturn(associado);
		when(associado.getId()).thenReturn(associadoId);
		when(associado.getCpf()).thenReturn(cpf);
		
		when(votoRepository.existsBySessaoIdAndAssociadoId(sessaoId, associadoId)).thenReturn(false);
		
		when(cpfValidationFacade.validate(cpf))
		.thenReturn(CpfValidationStatus.ABLE_TO_VOTE);

		when(votoRepository.saveAndFlush(any(Voto.class))).thenAnswer(invocation -> {
			Voto voto = invocation.getArgument(0);
			ReflectionTestUtils.setField(voto, "id", 1L);
			return voto;
		});

		VotoResponse response = votoService.votar(pautaId, request);

		assertEquals(1L, response.id());
		assertEquals(pautaId, response.pautaId());
		assertEquals(sessaoId, response.sessaoId());
		assertEquals(associadoId, response.associadoId());
		assertEquals(OpcaoVoto.SIM, response.opcao());

		verify(votoRepository).saveAndFlush(any(Voto.class));
	}

	@Test
	void shouldNotRegisterVoteWhenSessionIsExpired() {
		Long pautaId = 1L;

		CreateVotoRequest request = new CreateVotoRequest(1L, OpcaoVoto.SIM);
		SessaoVotacao sessao = mock(SessaoVotacao.class);

		when(sessaoVotacaoService.findEntityByPauta(pautaId)).thenReturn(sessao);
		when(sessao.isOpen()).thenReturn(false);

		assertThrows(ExpiredSessaoException.class, () -> votoService.votar(pautaId, request));

		verifyNoInteractions(associadoService);
		verifyNoInteractions(votoRepository);
	}

	@Test
	void shouldNotRegisterDuplicateVote() {
		Long pautaId = 1L;
		Long sessaoId = 1L;
		Long associadoId = 1L;

		CreateVotoRequest request = new CreateVotoRequest(associadoId, OpcaoVoto.NAO);
		SessaoVotacao sessao = mock(SessaoVotacao.class);
		Associado associado = mock(Associado.class);

		when(sessaoVotacaoService.findEntityByPauta(pautaId)).thenReturn(sessao);
		when(sessao.isOpen()).thenReturn(true);
		when(sessao.getId()).thenReturn(sessaoId);

		when(associadoService.findEntityById(associadoId)).thenReturn(associado);
		when(associado.getId()).thenReturn(associadoId);

		when(votoRepository.existsBySessaoIdAndAssociadoId(sessaoId, associadoId)).thenReturn(true);

		assertThrows(DuplicateVoteException.class, () -> votoService.votar(pautaId, request));

		verify(votoRepository, never()).saveAndFlush(any(Voto.class));
	}

	@Test
	void shouldConvertDatabaseConflictToDuplicateVoteException() {
		Long pautaId = 1L;
		Long sessaoId = 1L;
		Long associadoId = 1L;
		String cpf = "52998224725";

		CreateVotoRequest request = new CreateVotoRequest(associadoId, OpcaoVoto.SIM);
		SessaoVotacao sessao = org.mockito.Mockito.mock(SessaoVotacao.class);
		Associado associado = org.mockito.Mockito.mock(Associado.class);

		when(sessaoVotacaoService.findEntityByPauta(pautaId)).thenReturn(sessao);

		when(sessao.isOpen()).thenReturn(true);
		when(sessao.getId()).thenReturn(sessaoId);

		when(associadoService.findEntityById(associadoId)).thenReturn(associado);
		when(associado.getId()).thenReturn(associadoId);
		when(associado.getCpf()).thenReturn(cpf);
		when(cpfValidationFacade.validate(cpf))
				.thenReturn(CpfValidationStatus.ABLE_TO_VOTE);

		when(votoRepository.existsBySessaoIdAndAssociadoId(sessaoId, associadoId)).thenReturn(false);

		when(votoRepository.saveAndFlush(any(Voto.class)))
				.thenThrow(new DataIntegrityViolationException("Violação da constraint única"));

		assertThrows(CreateEntityException.class, () -> votoService.votar(pautaId, request));
	}
	
	@Test
	void shouldNotRegisterVoteWhenAssociadoIsUnableToVote() {
		Long pautaId = 1L;
		Long sessaoId = 1L;
		Long associadoId = 1L;
		String cpf = "52998224725";

		CreateVotoRequest request = new CreateVotoRequest(associadoId, OpcaoVoto.SIM);

		SessaoVotacao sessao = mock(SessaoVotacao.class);

		Associado associado = mock(Associado.class);

		when(sessaoVotacaoService.findEntityByPauta(pautaId)).thenReturn(sessao);

		when(sessao.isOpen()).thenReturn(true);
		when(sessao.getId()).thenReturn(sessaoId);

		when(associadoService.findEntityById(associadoId)).thenReturn(associado);

		when(associado.getId()).thenReturn(associadoId);
		when(associado.getCpf()).thenReturn(cpf);

		when(votoRepository.existsBySessaoIdAndAssociadoId(sessaoId, associadoId)).thenReturn(false);

		when(cpfValidationFacade.validate(cpf)).thenReturn(CpfValidationStatus.UNABLE_TO_VOTE);

		assertThrows(AssociadoUnableToVoteException.class, () -> votoService.votar(pautaId, request));

		verify(cpfValidationFacade).validate(cpf);
		verify(votoRepository, never()).saveAndFlush(any(Voto.class));
	}

	@Test
	void shouldNotRegisterVoteWhenCpfIsInvalid() {
		Long pautaId = 1L;
		Long sessaoId = 1L;
		Long associadoId = 1L;
		String cpf = "11111111111";

		CreateVotoRequest request = new CreateVotoRequest(associadoId, OpcaoVoto.SIM);

		SessaoVotacao sessao = mock(SessaoVotacao.class);

		Associado associado = mock(Associado.class);

		when(sessaoVotacaoService.findEntityByPauta(pautaId)).thenReturn(sessao);

		when(sessao.isOpen()).thenReturn(true);
		when(sessao.getId()).thenReturn(sessaoId);

		when(associadoService.findEntityById(associadoId)).thenReturn(associado);

		when(associado.getId()).thenReturn(associadoId);
		when(associado.getCpf()).thenReturn(cpf);

		when(votoRepository.existsBySessaoIdAndAssociadoId(sessaoId, associadoId)).thenReturn(false);

		when(cpfValidationFacade.validate(cpf)).thenThrow(new InvalidCpfException());

		assertThrows(InvalidCpfException.class, () -> votoService.votar(pautaId, request));

		verify(cpfValidationFacade).validate(cpf);
		verify(votoRepository, never()).saveAndFlush(any(Voto.class));
	}

}
