package com.desafio.votacao_api.service;

import com.desafio.votacao_api.dto.SessaoVotacaoRequestDTO;
import com.desafio.votacao_api.dto.SessaoVotacaoResponseDTO;
import com.desafio.votacao_api.dto.VotingResultDTO;
import com.desafio.votacao_api.exception.SessaoVotacaoNotFoundException;
import com.desafio.votacao_api.model.Pauta;
import com.desafio.votacao_api.model.SessaoVotacao;
import com.desafio.votacao_api.model.enums.VotoValue;
import com.desafio.votacao_api.repository.SessaoVotacaoRepository;
import com.desafio.votacao_api.repository.VotoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SessaoVotacaoServiceTest {

    @Mock
    private SessaoVotacaoRepository sessaoVotacaoRepository;

    @Mock
    private VotoRepository votoRepository;

    @Mock
    private PautaService pautaService;

    @InjectMocks
    private SessaoVotacaoService sessaoVotacaoService;

    @Test
    void openSessionShouldCreateSessionWithRequestedDuration() throws Exception {
        UUID pautaId = UUID.randomUUID();
        UUID sessaoId = UUID.randomUUID();
        Pauta pauta = createPautaWithId(pautaId);
        SessaoVotacaoRequestDTO request = new SessaoVotacaoRequestDTO(pautaId, 120);

        when(pautaService.getPautaByIdInternal(pautaId)).thenReturn(pauta);
        when(sessaoVotacaoRepository.save(any(SessaoVotacao.class))).thenAnswer(invocation -> {
            SessaoVotacao sessao = invocation.getArgument(0);
            setPrivateField(sessao, "id", sessaoId);
            return sessao;
        });

        SessaoVotacaoResponseDTO response = sessaoVotacaoService.openSession(request);

        assertEquals(sessaoId, response.id());
        assertEquals(pautaId, response.pautaId());
        assertEquals(120, Duration.between(response.createdAt(), response.finalSessionDateTime()).getSeconds());
        assertTrue(response.isActive());
        verify(sessaoVotacaoRepository, times(1)).save(any(SessaoVotacao.class));
    }

    @Test
    void openSessionShouldUseDefaultDurationWhenRequestDurationIsNull() throws Exception {
        UUID pautaId = UUID.randomUUID();
        Pauta pauta = createPautaWithId(pautaId);
        SessaoVotacaoRequestDTO request = new SessaoVotacaoRequestDTO(pautaId, null);

        when(pautaService.getPautaByIdInternal(pautaId)).thenReturn(pauta);
        when(sessaoVotacaoRepository.save(any(SessaoVotacao.class))).thenAnswer(invocation -> invocation.getArgument(0));

        SessaoVotacaoResponseDTO response = sessaoVotacaoService.openSession(request);

        assertEquals(60, Duration.between(response.createdAt(), response.finalSessionDateTime()).getSeconds());
    }

    @Test
    void getSessaoVotacaoByIdShouldReturnResponseWhenFound() throws Exception {
        UUID pautaId = UUID.randomUUID();
        UUID sessaoId = UUID.randomUUID();
        SessaoVotacao sessao = createSessionWithIds(sessaoId, pautaId, 60);

        when(sessaoVotacaoRepository.findById(sessaoId)).thenReturn(Optional.of(sessao));

        SessaoVotacaoResponseDTO response = sessaoVotacaoService.getSessaoVotacaoById(sessaoId);

        assertEquals(sessaoId, response.id());
        assertEquals(pautaId, response.pautaId());
        verify(sessaoVotacaoRepository, times(1)).findById(sessaoId);
    }

    @Test
    void getSessaoVotacaoByIdShouldThrowWhenNotFound() {
        UUID sessaoId = UUID.randomUUID();
        when(sessaoVotacaoRepository.findById(sessaoId)).thenReturn(Optional.empty());

        assertThrows(SessaoVotacaoNotFoundException.class, () -> sessaoVotacaoService.getSessaoVotacaoById(sessaoId));
    }

    @Test
    void getSessaoVotacaoByIdInternalShouldThrowWhenNotFound() {
        UUID sessaoId = UUID.randomUUID();
        when(sessaoVotacaoRepository.findById(sessaoId)).thenReturn(Optional.empty());

        assertThrows(SessaoVotacaoNotFoundException.class, () -> sessaoVotacaoService.getSessaoVotacaoByIdInternal(sessaoId));
    }

    @Test
    void getVotingResultsShouldReturnApprovedWhenYesVotesAreHigher() throws Exception {
        UUID pautaId = UUID.randomUUID();
        UUID sessaoId = UUID.randomUUID();
        SessaoVotacao sessao = createSessionWithIds(sessaoId, pautaId, 60);

        when(sessaoVotacaoRepository.findById(sessaoId)).thenReturn(Optional.of(sessao));
        when(votoRepository.countBySessaoVotacaoIdAndVoteValue(sessaoId, VotoValue.SIM)).thenReturn(7L);
        when(votoRepository.countBySessaoVotacaoIdAndVoteValue(sessaoId, VotoValue.NAO)).thenReturn(2L);

        VotingResultDTO result = sessaoVotacaoService.getVotingResults(sessaoId);

        assertEquals(sessaoId, result.sessaoVotacaoId());
        assertEquals(9L, result.totalVotes());
        assertEquals(7L, result.votesYes());
        assertEquals(2L, result.votesNo());
        assertEquals("APPROVED", result.result());
    }

    @Test
    void getVotingResultsShouldReturnRejectedWhenNoVotesAreHigher() throws Exception {
        UUID pautaId = UUID.randomUUID();
        UUID sessaoId = UUID.randomUUID();
        SessaoVotacao sessao = createSessionWithIds(sessaoId, pautaId, 60);

        when(sessaoVotacaoRepository.findById(sessaoId)).thenReturn(Optional.of(sessao));
        when(votoRepository.countBySessaoVotacaoIdAndVoteValue(sessaoId, VotoValue.SIM)).thenReturn(3L);
        when(votoRepository.countBySessaoVotacaoIdAndVoteValue(sessaoId, VotoValue.NAO)).thenReturn(8L);

        VotingResultDTO result = sessaoVotacaoService.getVotingResults(sessaoId);

        assertEquals(11L, result.totalVotes());
        assertEquals("REJECTED", result.result());
    }

    @Test
    void getVotingResultsShouldReturnTiedWhenVoteCountsAreEqual() throws Exception {
        UUID pautaId = UUID.randomUUID();
        UUID sessaoId = UUID.randomUUID();
        SessaoVotacao sessao = createSessionWithIds(sessaoId, pautaId, 60);

        when(sessaoVotacaoRepository.findById(sessaoId)).thenReturn(Optional.of(sessao));
        when(votoRepository.countBySessaoVotacaoIdAndVoteValue(sessaoId, VotoValue.SIM)).thenReturn(5L);
        when(votoRepository.countBySessaoVotacaoIdAndVoteValue(sessaoId, VotoValue.NAO)).thenReturn(5L);

        VotingResultDTO result = sessaoVotacaoService.getVotingResults(sessaoId);

        assertEquals(10L, result.totalVotes());
        assertEquals("TIED", result.result());
    }

    @Test
    void getVotingResultsShouldThrowWhenSessionDoesNotExist() {
        UUID sessaoId = UUID.randomUUID();
        when(sessaoVotacaoRepository.findById(sessaoId)).thenReturn(Optional.empty());

        assertThrows(SessaoVotacaoNotFoundException.class, () -> sessaoVotacaoService.getVotingResults(sessaoId));
        verify(votoRepository, never()).countBySessaoVotacaoIdAndVoteValue(any(UUID.class), any(VotoValue.class));
    }

    private static Pauta createPautaWithId(UUID pautaId) throws Exception {
        Pauta pauta = new Pauta("Pauta de investimentos", "Decisao sobre alocacao de recursos");
        setPrivateField(pauta, "id", pautaId);
        return pauta;
    }

    private static SessaoVotacao createSessionWithIds(UUID sessaoId, UUID pautaId, int durationInSeconds) throws Exception {
        Pauta pauta = createPautaWithId(pautaId);
        SessaoVotacao sessao = SessaoVotacao.openSession(pauta, durationInSeconds);
        setPrivateField(sessao, "id", sessaoId);
        return sessao;
    }

    private static void setPrivateField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
}
