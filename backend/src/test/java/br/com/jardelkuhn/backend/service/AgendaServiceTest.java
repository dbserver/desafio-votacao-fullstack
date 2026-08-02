package br.com.jardelkuhn.backend.service;

import br.com.jardelkuhn.backend.agenda.businessrule.AgendaStatusCalculator;
import br.com.jardelkuhn.backend.agenda.dto.CreateAgendaDTO;
import br.com.jardelkuhn.backend.agenda.dto.VoteDTO;
import br.com.jardelkuhn.backend.agenda.entity.*;
import br.com.jardelkuhn.backend.agenda.exception.*;
import br.com.jardelkuhn.backend.agenda.repository.AgendaRepository;

import br.com.jardelkuhn.backend.agenda.repository.VoteRepository;
import br.com.jardelkuhn.backend.agenda.service.AgendaService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Clock;
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

    @Mock
    private VoteRepository voteRepository;

    @InjectMocks
    private AgendaService agendaService;

    @Test
    @DisplayName("Should find all agendas with voting results")
    public void shouldFindAllAgendasWithConsolidation() {
        LocalDateTime limitDate = LocalDateTime.now().minusDays(1);

        AgendaVotingProjection projection =
                new AgendaVotingProjection.Builder(1L)
                        .setTitle("Agenda1")
                        .setOpenedDate(LocalDateTime.now().minusDays(2))
                        .setLimitDate(limitDate)
                        .setYesVotes(5L)
                        .setNoVotes(3L)
                        .setTotalVotes(8L)
                        .build();

        when(agendaRepository.findAllAgendaVotingProjection()).thenReturn(List.of(projection));

        List<AgendaVotingProjection> result = agendaService.findAllAgendaVotingProjection();

        assertEquals(1, result.size());

        AgendaVotingProjection agenda = result.getFirst();

        assertEquals(1L, agenda.id());
        assertEquals("Agenda1", agenda.title());
        assertEquals(AgendaStatus.FINISHED, new AgendaStatusCalculator(Clock.systemDefaultZone()).calculate(agenda.openedDate(), agenda.limitDate()));
        assertEquals(5L, agenda.yesVotes());
        assertEquals(3L, agenda.noVotes());
        assertEquals(8L, agenda.totalVotes());

        verify(agendaRepository).findAllAgendaVotingProjection();
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
                () -> agendaService.openAgendaAndReturnLimitDate(agendaId, 2)
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

        assertThrows(AlreadyOpenedException.class,
                () -> agendaService.openAgendaAndReturnLimitDate(agendaId, 2)
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

        assertThrows(DurationInvalidException.class,
                () -> agendaService.openAgendaAndReturnLimitDate(agendaId, 0)
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

        agendaService.openAgendaAndReturnLimitDate(agendaId, 30);

        ArgumentCaptor<Agenda> captor = ArgumentCaptor.forClass(Agenda.class);

        verify(agendaRepository).save(captor.capture());
        verify(agendaRepository, times(1)).save(agenda);

        Agenda savedAgenda = captor.getValue();

        assertNotNull(savedAgenda.getLimitDate());
        assertNotNull(savedAgenda.getOpenedDate());
        assertEquals(savedAgenda.getId(), agenda.getId());
        assertEquals(savedAgenda.getOpenedDate().plusMinutes(30), savedAgenda.getLimitDate());
    }

    @Test
    @DisplayName("Should throw not found when agenda does not exist")
    void shouldThrowNotFoundWhenAgendaDoesNotExist() {
        when(agendaRepository.findById(1L)).thenReturn(Optional.empty());

        VoteDTO dto = new VoteDTO("associate", VoteOption.YES);

        assertThrows(
                EntityNotFoundException.class,
                () -> agendaService.voteOnAgenda(1L, dto)
        );

        verify(agendaRepository).findById(1L);
        verifyNoInteractions(voteRepository);
    }

    @Test
    @DisplayName("Should throw exception when voting session has not started")
    void shouldThrowExceptionWhenVotingSessionNotStarted() {
        Agenda agenda = new Agenda();
        agenda.setOpenedDate(null);

        when(agendaRepository.findById(1L)).thenReturn(Optional.of(agenda));

        VoteDTO dto = new VoteDTO("associate", VoteOption.YES);

        assertThrows(
                NotStartedException.class,
                () -> agendaService.voteOnAgenda(1L, dto)
        );

        verify(voteRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when voting session is closed")
    void shouldThrowExceptionWhenVotingSessionClosed() {
        Agenda agenda = new Agenda();

        agenda.setOpenedDate(LocalDateTime.now().minusHours(2));
        agenda.setLimitDate(LocalDateTime.now().minusMinutes(1));

        when(agendaRepository.findById(1L)).thenReturn(Optional.of(agenda));

        VoteDTO dto = new VoteDTO("associate", VoteOption.YES);

        assertThrows(
                AlreadyClosedException.class,
                () -> agendaService.voteOnAgenda(1L, dto)
        );

        verify(voteRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when associate id is invalid")
    void shouldThrowExceptionWhenAssociateIsInvalid() {
        Agenda agenda = new Agenda();

        agenda.setOpenedDate(LocalDateTime.now().minusHours(1));
        agenda.setLimitDate(LocalDateTime.now().plusHours(4));

        when(agendaRepository.findById(1L)).thenReturn(Optional.of(agenda));

        VoteDTO dto = new VoteDTO("", VoteOption.YES);

        assertThrows(
                InvalidAssociateException.class,
                () -> agendaService.voteOnAgenda(1L, dto)
        );

        verify(voteRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when vote option is null")
    void shouldThrowExceptionWhenVoteOptionIsNull() {
        Agenda agenda = new Agenda();

        agenda.setOpenedDate(LocalDateTime.now().minusHours(1));
        agenda.setLimitDate(LocalDateTime.now().plusHours(4));

        when(agendaRepository.findById(1L)).thenReturn(Optional.of(agenda));

        VoteDTO dto = new VoteDTO("associate", null);

        assertThrows(
                InvalidVoteOptionException.class,
                () -> agendaService.voteOnAgenda(1L, dto)
        );

        verify(voteRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when associate already voted")
    void shouldThrowExceptionWhenAssociateAlreadyVoted() {
        Agenda agenda = new Agenda();

        agenda.setOpenedDate(LocalDateTime.now().minusHours(1));
        agenda.setLimitDate(LocalDateTime.now().plusHours(4));

        when(agendaRepository.findById(1L)).thenReturn(Optional.of(agenda));

        when(voteRepository.findByAgendaAndAssociateId(agenda, "associate")).thenReturn(Optional.of(new Vote()));

        VoteDTO dto = new VoteDTO("associate", VoteOption.YES);

        assertThrows(
                DuplicatedVoteException.class,
                () -> agendaService.voteOnAgenda(1L, dto)
        );

        verify(voteRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should save vote successfully")
    void shouldSaveVote() {
        Agenda agenda = new Agenda();

        agenda.setOpenedDate(LocalDateTime.now().minusHours(1));
        agenda.setLimitDate(LocalDateTime.now().plusHours(4));

        when(agendaRepository.findById(1L)).thenReturn(Optional.of(agenda));

        when(voteRepository.findByAgendaAndAssociateId(agenda, "associate")).thenReturn(Optional.empty());

        VoteDTO dto = new VoteDTO("associate", VoteOption.YES);

        agendaService.voteOnAgenda(1L, dto);

        ArgumentCaptor<Vote> captor = ArgumentCaptor.forClass(Vote.class);

        verify(voteRepository).save(captor.capture());

        Vote saved = captor.getValue();

        assertEquals(agenda, saved.getAgenda());
        assertEquals("associate", saved.getAssociateId());
        assertEquals(VoteOption.YES, saved.getVoteOption());
    }
}
