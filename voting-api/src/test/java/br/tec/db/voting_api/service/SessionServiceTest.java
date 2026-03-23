package br.tec.db.voting_api.service;

import br.tec.db.voting_api.domain.Agenda;
import br.tec.db.voting_api.domain.Session;
import br.tec.db.voting_api.dto.input.SessionInputDTO;
import br.tec.db.voting_api.exception.BusinessException;
import br.tec.db.voting_api.repository.AgendaRepository;
import br.tec.db.voting_api.repository.SessionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SessionServiceTest {

    @InjectMocks
    private SessionService sessionService;

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private AgendaRepository agendaRepository;

    @Test
    @DisplayName("Deve abrir sessão com duração padrão (1 minuto) quando duração não informada")
    void openSession_WhenValidDataAndNoPreviousSession_ShouldSaveSessionWithDefaultDuration() {
        // Arrange
        Long agendaId = 1L;
        Agenda agenda = new Agenda();
        agenda.setId(agendaId);

        SessionInputDTO inputDTO = new SessionInputDTO(null);

        when(agendaRepository.findById(agendaId)).thenReturn(Optional.of(agenda));
        when(sessionRepository.findByAgendaId(agendaId)).thenReturn(Optional.empty());

        // Act
        sessionService.openSession(agendaId, inputDTO);

        // Assert
        ArgumentCaptor<Session> sessionCaptor = ArgumentCaptor.forClass(Session.class);
        verify(sessionRepository, times(1)).save(sessionCaptor.capture());

        Session savedSession = sessionCaptor.getValue();
        assertEquals(agenda, savedSession.getAgenda());
        assertNotNull(savedSession.getOpeningDate());
        assertNotNull(savedSession.getClosingDate());
        
        long duration = Duration.between(savedSession.getOpeningDate(), savedSession.getClosingDate()).toMinutes();
        assertEquals(1L, duration);
    }

    @Test
    @DisplayName("Deve abrir sessão com duração personalizada")
    void openSession_WhenValidDataWithCustomDuration_ShouldSaveSessionWithCustomDuration() {
        // Arrange
        Long agendaId = 1L;
        Agenda agenda = new Agenda();
        agenda.setId(agendaId);

        SessionInputDTO inputDTO = new SessionInputDTO(5);

        when(agendaRepository.findById(agendaId)).thenReturn(Optional.of(agenda));
        when(sessionRepository.findByAgendaId(agendaId)).thenReturn(Optional.empty());

        // Act
        sessionService.openSession(agendaId, inputDTO);

        // Assert
        ArgumentCaptor<Session> sessionCaptor = ArgumentCaptor.forClass(Session.class);
        verify(sessionRepository, times(1)).save(sessionCaptor.capture());

        Session savedSession = sessionCaptor.getValue();
        
        long duration = Duration.between(savedSession.getOpeningDate(), savedSession.getClosingDate()).toMinutes();
        assertEquals(5L, duration);
    }

    @Test
    @DisplayName("Deve lançar exceção quando a pauta não for encontrada")
    void openSession_WhenAgendaNotFound_ShouldThrowException() {
        // Arrange
        Long agendaId = 1L;
        SessionInputDTO inputDTO = new SessionInputDTO(5);

        when(agendaRepository.findById(agendaId)).thenReturn(Optional.empty());

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, 
                () -> sessionService.openSession(agendaId, inputDTO));
        
        assertEquals("Pauta não encontrada!", exception.getMessage());
        verify(sessionRepository, never()).save(any(Session.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando já existir uma sessão aberta para a pauta")
    void openSession_WhenSessionAlreadyOpen_ShouldThrowException() {
        // Arrange
        Long agendaId = 1L;
        Agenda agenda = new Agenda();
        agenda.setId(agendaId);

        Session existingSession = new Session();
        existingSession.setClosingDate(LocalDateTime.now().plusMinutes(10));

        SessionInputDTO inputDTO = new SessionInputDTO(5);

        when(agendaRepository.findById(agendaId)).thenReturn(Optional.of(agenda));
        when(sessionRepository.findByAgendaId(agendaId)).thenReturn(Optional.of(existingSession));

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, 
                () -> sessionService.openSession(agendaId, inputDTO));
        
        assertEquals("Já existe uma sessão aberta para essa pauta", exception.getMessage());
        verify(sessionRepository, never()).save(any(Session.class));
    }

    @Test
    @DisplayName("Deve abrir nova sessão quando a sessão anterior já estiver fechada")
    void openSession_WhenPreviousSessionIsClosed_ShouldSaveNewSession() {
        // Arrange
        Long agendaId = 1L;
        Agenda agenda = new Agenda();
        agenda.setId(agendaId);

        Session existingSession = new Session();
        existingSession.setClosingDate(LocalDateTime.now().minusMinutes(5));

        SessionInputDTO inputDTO = new SessionInputDTO(10);

        when(agendaRepository.findById(agendaId)).thenReturn(Optional.of(agenda));
        when(sessionRepository.findByAgendaId(agendaId)).thenReturn(Optional.of(existingSession));

        // Act
        sessionService.openSession(agendaId, inputDTO);

        // Assert
        ArgumentCaptor<Session> sessionCaptor = ArgumentCaptor.forClass(Session.class);
        verify(sessionRepository, times(1)).save(sessionCaptor.capture());

        Session savedSession = sessionCaptor.getValue();
        
        long duration = Duration.between(savedSession.getOpeningDate(), savedSession.getClosingDate()).toMinutes();
        assertEquals(10L, duration);
    }
}
