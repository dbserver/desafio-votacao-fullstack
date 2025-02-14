package com.db.votacao.api.client;

import com.db.votacao.api.exception.ResourceNotFoundException;
import com.db.votacao.api.model.VotingResult;
import com.db.votacao.api.model.VotingSession;
import com.db.votacao.api.repository.VoteRepository;
import com.db.votacao.api.repository.VotingSessionRepository;
import com.db.votacao.api.service.RedisService;
import com.db.votacao.api.service.VotingResultService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class VotingResultServiceTest {

    @Mock
    private VoteRepository voteRepository;
    @Mock
    private VotingSessionRepository sessionRepository;
    @Mock
    private RedisService redisService;

    @InjectMocks
    private VotingResultService votingResultService;

    @Test
    void getVotingResult_Success() {
        VotingSession session = new VotingSession();
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(redisService.getVoteCount(1L, true)).thenReturn(5L);
        when(redisService.getVoteCount(1L, false)).thenReturn(3L);
        when(voteRepository.countBySessionAndVoteValue(any(), eq(true))).thenReturn(5L);
        when(voteRepository.countBySessionAndVoteValue(any(), eq(false))).thenReturn(3L);

        VotingResult result = votingResultService.getVotingResult(1L);

        assertThat(result).isNotNull();
        assertThat(result.getYesVotes()).isEqualTo(5L);
        assertThat(result.getNoVotes()).isEqualTo(3L);
    }

    @Test
    void getVotingResult_SessionNotFound() {
        when(sessionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> votingResultService.getVotingResult(1L))
                .isInstanceOf(ResourceNotFoundException.class);
    }
} 