package br.tec.db.voting_api.service;

import br.tec.db.voting_api.domain.Agenda;
import br.tec.db.voting_api.domain.Session;
import br.tec.db.voting_api.domain.Vow;
import br.tec.db.voting_api.domain.enums.VowType;
import br.tec.db.voting_api.dto.input.VowInputDTO;
import br.tec.db.voting_api.exception.BusinessException;
import br.tec.db.voting_api.external.cpf.CpfClient;
import br.tec.db.voting_api.external.cpf.CpfResponse;
import br.tec.db.voting_api.external.cpf.CpfStatus;
import br.tec.db.voting_api.repository.AgendaRepository;
import br.tec.db.voting_api.repository.SessionRepository;
import br.tec.db.voting_api.repository.VowRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VowServiceTest {

    @InjectMocks
    private VowService vowService;

    @Mock
    private VowRepository vowRepository;

    @Mock
    private AgendaRepository agendaRepository;

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private CpfClient cpfClient;

    @Test
    @DisplayName("Deve registrar o voto quando a sessão estiver aberta e o associado ainda não votou")
    void shouldRegisterVoteWhenSessionOpenAndAssociateDidNotVote() {
        // Arrange
        Long agendaId = 1L;
        Agenda agenda = new Agenda();
        agenda.setId(agendaId);

        Session session = new Session();
        session.setClosingDate(LocalDateTime.now().plusMinutes(5));

        VowInputDTO vowInputDTO = new VowInputDTO(agenda.getId(), "12345", VowType.YES);

        when(agendaRepository.findById(agendaId)).thenReturn(Optional.of(agenda));
        when(sessionRepository.findByAgendaId(agendaId)).thenReturn(Optional.of(session));
        when(vowRepository.existsByAssociatedIdAndAgendaId("12345", agendaId)).thenReturn(false);

        CpfResponse response = new CpfResponse(CpfStatus.ABLE_TO_VOTE);
        when(cpfClient.validateCPF(vowInputDTO.associatedId())).thenReturn(response);

        // Act
        vowService.registerVote(vowInputDTO);

        // Assert
        verify(vowRepository, times(1)).save(any(Vow.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando o associado já tiver votado na pauta")
    void shouldThrowErrorWhenMemberVoted() {
        // Arrange
        Long agendaId = 1L;
        Agenda agenda = new Agenda();
        agenda.setId(agendaId);

        Session session = new Session();
        session.setClosingDate(LocalDateTime.now().plusMinutes(5));

        VowInputDTO vowInputDTO = new VowInputDTO(agenda.getId(), "12345", VowType.YES);

        when(agendaRepository.findById(agendaId)).thenReturn(Optional.of(agenda));
        when(sessionRepository.findByAgendaId(agendaId)).thenReturn(Optional.of(session));
        when(vowRepository.existsByAssociatedIdAndAgendaId("12345", agendaId)).thenReturn(true);

        // Act & Assert
        assertThrows(BusinessException.class, () -> vowService.registerVote(vowInputDTO));
    }

    @Test
    @DisplayName("Deve lançar exceção quando a sessão estiver encerrada")
    void shouldThrowErrorWhenSessionClose() {
        // Arrange
        Long agendaId = 1L;
        Agenda agenda = new Agenda();
        agenda.setId(agendaId);

        Session session = new Session();
        session.setClosingDate(LocalDateTime.now().minusMinutes(1));

        VowInputDTO vowInputDTO = new VowInputDTO(agenda.getId(), "12345", VowType.YES);

        when(agendaRepository.findById(agendaId)).thenReturn(Optional.of(agenda));
        when(sessionRepository.findByAgendaId(agendaId)).thenReturn(Optional.of(session));

        // Act & Assert
        assertThrows(BusinessException.class, () -> vowService.registerVote(vowInputDTO));
    }
}
