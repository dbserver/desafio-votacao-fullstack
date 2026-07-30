package br.com.jardelkuhn.backend.service;

import br.com.jardelkuhn.backend.dto.CreateAgendaDTO;
import br.com.jardelkuhn.backend.entity.Agenda;
import br.com.jardelkuhn.backend.repository.AgendaRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

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
}
