package com.desafio.votacao_api.service;

import com.desafio.votacao_api.dto.VotoRequestDTO;
import com.desafio.votacao_api.dto.VotoResponseDTO;
import com.desafio.votacao_api.exception.CpfNotFoundException;
import com.desafio.votacao_api.exception.CpfAlreadyVotedOnPautaException;
import com.desafio.votacao_api.exception.InvalidCpfException;
import com.desafio.votacao_api.exception.SessionClosedException;
import com.desafio.votacao_api.exception.UnableToVoteException;
import com.desafio.votacao_api.exception.VotoNotFoundException;
import com.desafio.votacao_api.model.Cpf;
import com.desafio.votacao_api.model.Pauta;
import com.desafio.votacao_api.model.SessaoVotacao;
import com.desafio.votacao_api.model.Voto;
import com.desafio.votacao_api.model.enums.VotoValue;
import com.desafio.votacao_api.repository.VotoRepository;
import com.desafio.votacao_api.service.facade.CpfValidationFacade;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VotoServiceTest {

    @Mock
    private VotoRepository votoRepository;

    @Mock
    private SessaoVotacaoService sessaoVotacaoService;

    @Mock
    private CpfValidationFacade cpfValidationFacade;

    @InjectMocks
    private VotoService votoService;

    @Test
    void registerVoteShouldSaveAndReturnResponseWhenSessionIsOpen() throws Exception {
        UUID sessaoId = UUID.randomUUID();
        UUID votoId = UUID.randomUUID();
        SessaoVotacao sessao = createOpenSession(sessaoId);
        VotoRequestDTO request = new VotoRequestDTO(sessaoId, "12345678901", VotoValue.SIM);

        when(sessaoVotacaoService.getSessaoVotacaoByIdInternal(sessaoId)).thenReturn(sessao);
        when(votoRepository.save(any(Voto.class))).thenAnswer(invocation -> {
            Voto voto = invocation.getArgument(0);
            setPrivateField(voto, "id", votoId);
            return voto;
        });

        VotoResponseDTO response = votoService.registerVote(request);

        assertEquals(votoId, response.id());
        assertEquals(sessaoId, response.sessaoVotacaoId());
        assertEquals(VotoValue.SIM, response.vote());

        ArgumentCaptor<Voto> voteCaptor = ArgumentCaptor.forClass(Voto.class);
        verify(votoRepository, times(1)).save(voteCaptor.capture());
        assertEquals(new Cpf("12345678901"), voteCaptor.getValue().getCpf());
        verify(cpfValidationFacade, times(1)).validateCpfNumber("12345678901");
    }

    @Test
    void registerVoteShouldThrowWhenSessionIsClosed() throws Exception {
        UUID sessaoId = UUID.randomUUID();
        SessaoVotacao sessao = createOpenSession(sessaoId);
        sessao.setFinalSessionDateTime(Instant.now().minusSeconds(10));

        VotoRequestDTO request = new VotoRequestDTO(sessaoId, "12345678901", VotoValue.NAO);

        when(sessaoVotacaoService.getSessaoVotacaoByIdInternal(sessaoId)).thenReturn(sessao);

        assertThrows(SessionClosedException.class, () -> votoService.registerVote(request));
        verify(cpfValidationFacade, never()).validateCpfNumber(any(String.class));
        verify(votoRepository, never()).save(any(Voto.class));
    }

    @Test
    void registerVoteShouldThrowWhenCpfIsInvalid() throws Exception {
        UUID sessaoId = UUID.randomUUID();
        SessaoVotacao sessao = createOpenSession(sessaoId);
        VotoRequestDTO request = new VotoRequestDTO(sessaoId, "123", VotoValue.SIM);

        when(sessaoVotacaoService.getSessaoVotacaoByIdInternal(sessaoId)).thenReturn(sessao);

        assertThrows(InvalidCpfException.class, () -> votoService.registerVote(request));
        verify(cpfValidationFacade, times(1)).validateCpfNumber("123");
        verify(votoRepository, never()).save(any(Voto.class));
    }

    @Test
    void registerVoteShouldThrowWhenCpfNotFoundInEligibilityValidation() throws Exception {
        UUID sessaoId = UUID.randomUUID();
        SessaoVotacao sessao = createOpenSession(sessaoId);
        VotoRequestDTO request = new VotoRequestDTO(sessaoId, "12345678901", VotoValue.SIM);

        when(sessaoVotacaoService.getSessaoVotacaoByIdInternal(sessaoId)).thenReturn(sessao);
        doThrow(new CpfNotFoundException("CPF not found."))
                .when(cpfValidationFacade)
                .validateCpfNumber("12345678901");

        assertThrows(CpfNotFoundException.class, () -> votoService.registerVote(request));
        verify(votoRepository, never()).save(any(Voto.class));
    }

    @Test
    void registerVoteShouldThrowWhenCpfIsUnableToVoteInEligibilityValidation() throws Exception {
        UUID sessaoId = UUID.randomUUID();
        SessaoVotacao sessao = createOpenSession(sessaoId);
        VotoRequestDTO request = new VotoRequestDTO(sessaoId, "12345678901", VotoValue.NAO);

        when(sessaoVotacaoService.getSessaoVotacaoByIdInternal(sessaoId)).thenReturn(sessao);
        doThrow(new UnableToVoteException("CPF is unable to vote."))
                .when(cpfValidationFacade)
                .validateCpfNumber("12345678901");

        assertThrows(UnableToVoteException.class, () -> votoService.registerVote(request));
        verify(votoRepository, never()).save(any(Voto.class));
    }

    @Test
    void registerVoteShouldThrowWhenCpfAlreadyVotedOnPauta() throws Exception {
        UUID sessaoId = UUID.randomUUID();
        SessaoVotacao sessao = createOpenSession(sessaoId);
        UUID pautaId = sessao.getPauta().getId();
        VotoRequestDTO request = new VotoRequestDTO(sessaoId, "12345678901", VotoValue.SIM);

        when(sessaoVotacaoService.getSessaoVotacaoByIdInternal(sessaoId)).thenReturn(sessao);
        when(votoRepository.existsActiveVoteByPautaIdAndCpf(pautaId, "12345678901")).thenReturn(true);

        assertThrows(CpfAlreadyVotedOnPautaException.class, () -> votoService.registerVote(request));
        verify(cpfValidationFacade, never()).validateCpfNumber(any(String.class));
        verify(votoRepository, never()).save(any(Voto.class));
    }

    @Test
    void getVotoByIdShouldReturnResponseWhenFound() throws Exception {
        UUID sessaoId = UUID.randomUUID();
        UUID votoId = UUID.randomUUID();
        Voto voto = createVoteWithIds(votoId, sessaoId, VotoValue.NAO, "12345678901");

        when(votoRepository.findById(votoId)).thenReturn(Optional.of(voto));

        VotoResponseDTO response = votoService.getVotoById(votoId);

        assertEquals(votoId, response.id());
        assertEquals(sessaoId, response.sessaoVotacaoId());
        assertEquals(VotoValue.NAO, response.vote());
    }

    @Test
    void getVotoByIdShouldThrowWhenNotFound() {
        UUID votoId = UUID.randomUUID();
        when(votoRepository.findById(votoId)).thenReturn(Optional.empty());

        assertThrows(VotoNotFoundException.class, () -> votoService.getVotoById(votoId));
    }

    @Test
    void getVotoByIdInternalShouldThrowWhenNotFound() {
        UUID votoId = UUID.randomUUID();
        when(votoRepository.findById(votoId)).thenReturn(Optional.empty());

        assertThrows(VotoNotFoundException.class, () -> votoService.getVotoByIdInternal(votoId));
    }

    private static SessaoVotacao createOpenSession(UUID sessaoId) throws Exception {
        Pauta pauta = new Pauta("Pauta economica", "Votacao de proposta tributaria");
        setPrivateField(pauta, "id", UUID.randomUUID());
        SessaoVotacao sessao = SessaoVotacao.openSession(pauta, 60);
        setPrivateField(sessao, "id", sessaoId);
        return sessao;
    }

    private static Voto createVoteWithIds(UUID votoId, UUID sessaoId, VotoValue votoValue, String cpf) throws Exception {
        SessaoVotacao sessao = createOpenSession(sessaoId);
        Voto voto = new Voto(sessao, votoValue, new Cpf(cpf));
        setPrivateField(voto, "id", votoId);
        return voto;
    }

    private static void setPrivateField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
}

