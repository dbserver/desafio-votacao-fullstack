package br.com.jardelkuhn.backend.service;

import br.com.jardelkuhn.backend.dto.*;
import br.com.jardelkuhn.backend.entity.Agenda;
import br.com.jardelkuhn.backend.repository.AgendaRepository;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentCaptor;
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
public class AgendaServiceTest {
    @Mock
    private AgendaRepository agendaRepository;

    @InjectMocks
    private AgendaService agendaService;

    @Test
    @DisplayName("Should find all agendas")
    public void shouldListAllAgendas() {
        Agenda a1 = new Agenda();
        a1.setId(1L);
        a1.setTitle("Agenda 1");
        a1.setDescription("descrição 1");

        Agenda a2 = new Agenda();
        a2.setId(2L);
        a2.setTitle("Agenda 2");
        a2.setDescription("descrição 2");

        when(agendaRepository.findAll()).thenReturn(List.of(a1, a2));

        List<Agenda> result = agendaService.listAllAgendas();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(agendaRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should create an agenda")
    public void shouldCreateAgenda() {
        CreateAgendaDTO dto = new CreateAgendaDTO("Agenda", "Descrição");

        Agenda saved = new Agenda();
        saved.setId(1L);
        saved.setTitle(dto.title());
        saved.setDescription(dto.description());

        when(agendaRepository.save(any(Agenda.class))).thenReturn(saved);

        Agenda result = agendaService.createAgenda(dto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Agenda", result.getTitle());
        assertEquals("Descrição", result.getDescription());
        assertNull(result.getOpenedDate());
        assertNull(result.getLimitDate());

        verify(agendaRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when agenda does not exist")
    void shouldThrowEntityNotFoundException() {
        Long agendaId = 1L;

        when(agendaRepository.findById(agendaId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> agendaService.openSessionAndReturnLimitDate(agendaId, 2)
        );

        assertEquals("Agenda com id 1 não encontrada", exception.getMessage());

        verify(agendaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw AgendaAlreadyOpenedException")
    void shouldThrowAgendaAlreadyOpenedException() {
        Long agendaId = 1L;

        Agenda agenda = new Agenda();
        agenda.setId(agendaId);
        agenda.setOpenedDate(LocalDateTime.now());

        when(agendaRepository.findById(agendaId))
                .thenReturn(Optional.of(agenda));

        assertThrows(AgendaAlreadyOpenedException.class,
                () -> agendaService.openSessionAndReturnLimitDate(agendaId, 2)
        );

        verify(agendaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw AgendaDurationInvalidException")
    void shouldThrowAgendaDurationInvalidException() {
        Long agendaId = 1L;

        Agenda agenda = new Agenda();
        agenda.setId(agendaId);

        when(agendaRepository.findById(agendaId))
                .thenReturn(Optional.of(agenda));

        assertThrows(AgendaDurationInvalidException.class,
                () -> agendaService.openSessionAndReturnLimitDate(agendaId, 0)
        );

        verify(agendaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should open an agenda session correctly")
    public void shouldOpenSessionCorrectly() {
        Long agendaId = 1L;

        Agenda agenda = new Agenda();
        agenda.setId(agendaId);

        when(agendaRepository.findById(agendaId)).thenReturn(Optional.of(agenda));

        agendaService.openSessionAndReturnLimitDate(agendaId, 30);

        ArgumentCaptor<Agenda> captor = ArgumentCaptor.forClass(Agenda.class);

        verify(agendaRepository).save(captor.capture());
        verify(agendaRepository, times(1)).save(agenda);

        Agenda savedAgenda = captor.getValue();

        assertNotNull(savedAgenda.getLimitDate());
        assertNotNull(savedAgenda.getOpenedDate());
        assertEquals(savedAgenda.getId(), agenda.getId());
        assertEquals(savedAgenda.getOpenedDate().plusMinutes(30), savedAgenda.getLimitDate());
    }
}
