package br.tec.db.voting_api.service;

import br.tec.db.voting_api.domain.Agenda;
import br.tec.db.voting_api.dto.output.VoteResultOutputDTO;
import br.tec.db.voting_api.exception.BusinessException;
import br.tec.db.voting_api.repository.AgendaRepository;
import br.tec.db.voting_api.repository.VowRepository;
import br.tec.db.voting_api.repository.projection.VotingResultProjection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VoteResultServiceTest {

    @InjectMocks
    private VoteResultService voteResultService;

    @Mock
    private AgendaRepository agendaRepository;

    @Mock
    private VowRepository vowRepository;

    @Mock
    private VotingResultProjection votingResultProjection;

    @Test
    @DisplayName("Deve retornar o resultado com vitória do Sim")
    void getVowsResults_WhenMoreYesVotes_ShouldReturnResultSim() {
        // Arrange
        Long agendaId = 1L;
        Agenda agenda = new Agenda();
        agenda.setId(agendaId);

        when(agendaRepository.findById(agendaId)).thenReturn(Optional.of(agenda));
        when(vowRepository.countVotes(agendaId)).thenReturn(10L);
        
        when(votingResultProjection.getTotalYes()).thenReturn(7L);
        when(votingResultProjection.getTotalNo()).thenReturn(3L);
        when(vowRepository.countResult(agendaId)).thenReturn(votingResultProjection);

        // Act
        VoteResultOutputDTO result = voteResultService.getVowsResults(agendaId);

        // Assert
        assertNotNull(result);
        assertEquals(agendaId, result.agendaId());
        assertEquals(7L, result.totalYes());
        assertEquals(3L, result.totalNo());
        assertEquals(10L, result.numberOfVotes());
        assertEquals("Sim", result.result());
    }

    @Test
    @DisplayName("Deve retornar o resultado com vitória do Não")
    void getVowsResults_WhenMoreNoVotes_ShouldReturnResultNao() {
        // Arrange
        Long agendaId = 1L;
        Agenda agenda = new Agenda();
        agenda.setId(agendaId);

        when(agendaRepository.findById(agendaId)).thenReturn(Optional.of(agenda));
        when(vowRepository.countVotes(agendaId)).thenReturn(15L);
        
        when(votingResultProjection.getTotalYes()).thenReturn(5L);
        when(votingResultProjection.getTotalNo()).thenReturn(10L);
        when(vowRepository.countResult(agendaId)).thenReturn(votingResultProjection);

        // Act
        VoteResultOutputDTO result = voteResultService.getVowsResults(agendaId);

        // Assert
        assertNotNull(result);
        assertEquals(agendaId, result.agendaId());
        assertEquals(5L, result.totalYes());
        assertEquals(10L, result.totalNo());
        assertEquals(15L, result.numberOfVotes());
        assertEquals("Não", result.result());
    }

    @Test
    @DisplayName("Deve retornar o resultado como Empate quando houver a mesma quantidade de votos")
    void getVowsResults_WhenEqualVotes_ShouldReturnResultEmpate() {
        // Arrange
        Long agendaId = 1L;
        Agenda agenda = new Agenda();
        agenda.setId(agendaId);

        when(agendaRepository.findById(agendaId)).thenReturn(Optional.of(agenda));
        when(vowRepository.countVotes(agendaId)).thenReturn(20L);
        
        when(votingResultProjection.getTotalYes()).thenReturn(10L);
        when(votingResultProjection.getTotalNo()).thenReturn(10L);
        when(vowRepository.countResult(agendaId)).thenReturn(votingResultProjection);

        // Act
        VoteResultOutputDTO result = voteResultService.getVowsResults(agendaId);

        // Assert
        assertNotNull(result);
        assertEquals(agendaId, result.agendaId());
        assertEquals(10L, result.totalYes());
        assertEquals(10L, result.totalNo());
        assertEquals(20L, result.numberOfVotes());
        assertEquals("Empate", result.result());
    }

    @Test
    @DisplayName("Deve tratar valores nulos da projeção como zero e retornar Empate")
    void getVowsResults_WhenProjectionReturnsNullValues_ShouldTreatAsZeroAndReturnEmpate() {
        // Arrange
        Long agendaId = 1L;
        Agenda agenda = new Agenda();
        agenda.setId(agendaId);

        when(agendaRepository.findById(agendaId)).thenReturn(Optional.of(agenda));
        when(vowRepository.countVotes(agendaId)).thenReturn(0L);
        
        when(votingResultProjection.getTotalYes()).thenReturn(null);
        when(votingResultProjection.getTotalNo()).thenReturn(null);
        when(vowRepository.countResult(agendaId)).thenReturn(votingResultProjection);

        // Act
        VoteResultOutputDTO result = voteResultService.getVowsResults(agendaId);

        // Assert
        assertNotNull(result);
        assertEquals(agendaId, result.agendaId());
        assertEquals(0L, result.totalYes());
        assertEquals(0L, result.totalNo());
        assertEquals(0L, result.numberOfVotes());
        assertEquals("Empate", result.result());
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar resultado de uma pauta não encontrada")
    void getVowsResults_WhenAgendaNotFound_ShouldThrowException() {
        // Arrange
        Long agendaId = 1L;

        when(agendaRepository.findById(agendaId)).thenReturn(Optional.empty());

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, 
                () -> voteResultService.getVowsResults(agendaId));
        
        assertEquals("Pauta não encontrada", exception.getMessage());
        verify(vowRepository, never()).countVotes(anyLong());
        verify(vowRepository, never()).countResult(anyLong());
    }
}
