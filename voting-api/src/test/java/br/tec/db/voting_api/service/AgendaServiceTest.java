package br.tec.db.voting_api.service;

import br.tec.db.voting_api.domain.Agenda;
import br.tec.db.voting_api.domain.Session;
import br.tec.db.voting_api.dto.input.AgendaInputDTO;
import br.tec.db.voting_api.dto.output.AgendaDetailOutputDTO;
import br.tec.db.voting_api.dto.output.AgendaOutputDTO;
import br.tec.db.voting_api.repository.AgendaRepository;
import br.tec.db.voting_api.repository.SessionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AgendaServiceTest {

    @Mock
    private AgendaRepository agendaRepository;

    @Mock
    private SessionRepository sessionRepository;

    @InjectMocks
    private AgendaService agendaService;

    @Test
    @DisplayName("Deve criar uma Pauta com sucesso e retornar AgendaOutputDTO")
    void createAgenda_ShouldReturnAgendaOutputDTO() {
        // Arrange
        var inputDTO = new AgendaInputDTO("Pauta 1", "Descrição 1");
        var savedAgenda = Agenda.builder()
                .title("Pauta 1")
                .description("Descrição 1")
                .build();
        savedAgenda.setId(1L);

        when(agendaRepository.save(any(Agenda.class))).thenReturn(savedAgenda);

        // Act
        AgendaOutputDTO result = agendaService.createAgenda(inputDTO);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Pauta 1", result.title());
        assertEquals("Descrição 1", result.description());
        verify(agendaRepository, times(1)).save(any(Agenda.class));
    }

    @Test
    @DisplayName("Deve retornar a lista de todas as Pautas (AgendaOutputDTO)")
    void getAllAgendas_ShouldReturnListOfAgendaOutputDTO() {
        // Arrange
        var agenda1 = Agenda.builder().title("Pauta 1").description("Desc 1").build();
        agenda1.setId(1L);
        var agenda2 = Agenda.builder().title("Pauta 2").description("Desc 2").build();
        agenda2.setId(2L);

        when(agendaRepository.findAll()).thenReturn(List.of(agenda1, agenda2));

        // Act
        List<AgendaOutputDTO> result = agendaService.getAllAgendas();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Pauta 1", result.get(0).title());
        assertEquals("Pauta 2", result.get(1).title());
        verify(agendaRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve buscar uma Pauta por ID (Sem Sessão Criada)")
    void getAgendaById_WhenNoSession_ShouldReturnDetailWithoutSession() {
        // Arrange
        var agenda = Agenda.builder().title("Pauta 1").description("Desc 1").build();
        agenda.setId(1L);
        agenda.setCreatedAt(LocalDateTime.now());

        when(agendaRepository.findById(1L)).thenReturn(Optional.of(agenda));
        when(sessionRepository.findByAgendaId(1L)).thenReturn(Optional.empty());

        // Act
        AgendaDetailOutputDTO result = agendaService.getAgendaById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.id());
        assertFalse(result.createdSession());
        assertFalse(result.sessionOpen());
        assertNull(result.timer());
        verify(agendaRepository, times(1)).findById(1L);
        verify(sessionRepository, times(1)).findByAgendaId(1L);
    }

    @Test
    @DisplayName("Deve buscar uma Pauta por ID (Com Sessão Aberta)")
    void getAgendaById_WhenSessionOpen_ShouldReturnDetailWithOpenSession() {
        // Arrange
        var agenda = Agenda.builder().title("Pauta 1").description("Desc 1").build();
        agenda.setId(1L);
        agenda.setCreatedAt(LocalDateTime.now());

        var session = Session.builder()
                .agenda(agenda)
                .closingDate(LocalDateTime.now().plusHours(1))
                .build();
        session.setId(1L);

        when(agendaRepository.findById(1L)).thenReturn(Optional.of(agenda));
        when(sessionRepository.findByAgendaId(1L)).thenReturn(Optional.of(session));

        // Act
        AgendaDetailOutputDTO result = agendaService.getAgendaById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.id());
        assertTrue(result.createdSession());
        assertTrue(result.sessionOpen());
        assertNotNull(result.timer());
    }

    @Test
    @DisplayName("Deve buscar uma Pauta por ID (Com Sessão Fechada)")
    void getAgendaById_WhenSessionClosed_ShouldReturnDetailWithClosedSession() {
        // Arrange
        var agenda = Agenda.builder().title("Pauta 1").description("Desc 1").build();
        agenda.setId(1L);
        agenda.setCreatedAt(LocalDateTime.now());

        var session = Session.builder()
                .agenda(agenda)
                .closingDate(LocalDateTime.now().minusHours(1))
                .build();
        session.setId(1L);

        when(agendaRepository.findById(1L)).thenReturn(Optional.of(agenda));
        when(sessionRepository.findByAgendaId(1L)).thenReturn(Optional.of(session));

        // Act
        AgendaDetailOutputDTO result = agendaService.getAgendaById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.id());
        assertTrue(result.createdSession());
        assertFalse(result.sessionOpen());
        assertNotNull(result.timer());
    }

    @Test
    @DisplayName("Deve lançar Exceção ao buscar uma Pauta inexistente")
    void getAgendaById_WhenAgendaNotFound_ShouldThrowException() {
        // Arrange
        when(agendaRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> agendaService.getAgendaById(1L));
        assertEquals("Pauta não encontrada", exception.getMessage());
        verify(agendaRepository, times(1)).findById(1L);
        verify(sessionRepository, never()).findByAgendaId(any());
    }
}
