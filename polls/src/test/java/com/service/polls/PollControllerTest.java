package com.service.polls;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.service.polls.adapters.in.web.PollController;
import com.service.polls.application.dto.PollDTO;
import com.service.polls.application.dto.VoteDTO;
import com.service.polls.application.enums.AnswerEnum;
import com.service.polls.domain.model.Poll;
import com.service.polls.ports.in.ICreatePollUseCase;
import com.service.polls.ports.in.IGetPollsUseCase;
import com.service.polls.ports.in.IListPollsUseCase;
import com.service.polls.ports.in.IVoteUseCase;
import com.service.polls.ports.out.IPollRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import tools.jackson.databind.ObjectMapper;

import java.time.Instant;
import java.time.temporal.ChronoUnit;


@SpringBootTest
public class PollControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private PollController pollController;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private IPollRepository pollRepository;

    @Autowired
    private ICreatePollUseCase createPollUseCase;

    @Autowired
    private IListPollsUseCase listPollsUseCase;

    @Autowired
    private IVoteUseCase voteUseCase;

    @Autowired
    private IGetPollsUseCase getPollsUseCase;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(pollController)
                .build();
    }


    @Test
    void shouldCreatePoll() throws Exception {

        PollDTO pollDTO = new PollDTO("Teste", 1);

        mockMvc.perform(post("/polls")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pollDTO)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void shouldVoteWithSuccess() throws Exception {

        Instant now = Instant.now();

        Mockito.when(pollRepository.findById(1L))
                .thenReturn(Poll.builder().title("Test").startsAt(now).endsAt(now.plus(2, ChronoUnit.MINUTES)).build());

        VoteDTO voteDTO = new VoteDTO(1L, "123456", AnswerEnum.YES);

        mockMvc.perform(post("/polls/vote")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(voteDTO)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void shouldNotVoteBecauseExpire() throws Exception {

        Instant now = Instant.now();

        Mockito.when(pollRepository.findById(1L))
                .thenReturn(Poll.builder().title("Test").startsAt(now).endsAt(now.minus(2, ChronoUnit.MINUTES)).build());

        VoteDTO voteDTO = new VoteDTO(1L, "123456", AnswerEnum.YES);

        mockMvc.perform(post("/polls/vote")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(voteDTO)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}
