package com.votacao.desafiovotacao.services;

import com.votacao.desafiovotacao.domain.entities.Agenda;
import com.votacao.desafiovotacao.domain.entities.Associated;
import com.votacao.desafiovotacao.domain.entities.Session;
import com.votacao.desafiovotacao.domain.entities.Vote;
import com.votacao.desafiovotacao.domain.services.VoteService;
import com.votacao.desafiovotacao.infra.AssociatedRepository;
import com.votacao.desafiovotacao.infra.VoteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VoteServiceTest {

    @Mock
    VoteRepository voteRepository;

    @InjectMocks
    VoteService voteService;

    @Mock
    AssociatedRepository associatedRepository;

    @Test
    void shouldSelectVotesPerSession() {
        List<Vote> expectedListVote = Collections.singletonList(buildVote());

        given(voteService.findAllVotesBySessionById("1")).willReturn(expectedListVote);
        List<Vote> resultListVote = voteService.findAllVotesBySessionById("1");

        assertThat(expectedListVote).isEqualTo(resultListVote);
    }

    @Test
    void shouldReturnEmptyListOfVotesPerSession() {
        List<Vote> resultListVote = voteService.findAllVotesBySessionById("0");
        assertThat(resultListVote).isEmpty();
    }

    @Test
    void associatedAlreadyVoted() {
        Session session = buildSession();
        List<Vote> expectedListVote = Collections.singletonList(buildVote());

        String associateId = "321";
        lenient().when(voteRepository.findAllBySession(Optional.ofNullable(session))).thenReturn(expectedListVote);

        Boolean resultAlreadyVote = voteService.validateAssociateAlreadyVote(Optional.ofNullable(session), associateId);
        assertThat(resultAlreadyVote).isTrue();
    }

    @Test
    void associatedDidNotVote() {
        Session session = buildSession();
        List<Vote> expectedListVote = Collections.emptyList();

        String associateId = "321";
        lenient().when(voteRepository.findAllBySession(Optional.ofNullable(session))).thenReturn(expectedListVote);

        Boolean resultAlreadyVote = voteService.validateAssociateAlreadyVote(Optional.ofNullable(session), associateId);
        assertThat(resultAlreadyVote).isFalse();
    }

    @Test
    public void AssociatedCpfAlreadyExists() {
        when(associatedRepository.findByCpf(any())).thenReturn(true);
        assertThat(associatedRepository.findByCpf(any())).isTrue();
    }

    private Agenda buildAgenda() {
        return Agenda.builder()
                .id("123")
                .title("Unit Test")
                .description("Unit test execution")
                .build();
    }

    private Session buildSession() {
        buildAgenda();
        return Session.builder()
                .id("1")
                .agendaId("123")
                .status(Session.StatusSession.ABERTA)
                .build();
    }

    private Associated buildAssociated() {
        return Associated.builder()
                .id("321")
                .name("Tester")
                .cpf("00401509095")
                .build();
    }

    private Vote buildVote() {
        buildAssociated();
        return Vote.builder()
                .id("123")
                .vote("SIM")
                .associatedId("321")
                .session(buildSession())
                .build();
    }
}
