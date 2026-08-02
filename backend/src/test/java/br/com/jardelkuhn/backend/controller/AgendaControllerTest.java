package br.com.jardelkuhn.backend.controller;

import br.com.jardelkuhn.backend.agenda.dto.CreateAgendaDTO;
import br.com.jardelkuhn.backend.agenda.dto.OpenSessionDTO;
import br.com.jardelkuhn.backend.agenda.dto.VoteDTO;
import br.com.jardelkuhn.backend.agenda.entity.Agenda;
import br.com.jardelkuhn.backend.agenda.entity.AgendaVotingProjection;
import br.com.jardelkuhn.backend.agenda.entity.VoteOption;
import br.com.jardelkuhn.backend.agenda.service.AgendaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class AgendaControllerTest {

    @MockitoBean
    private AgendaService agendaService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
    }

    @Test
    @DisplayName("should create a new agenda with valid properties")
    public void shouldCreateAgenda() throws Exception {
        Agenda agenda = new Agenda();
        agenda.setTitle("Agenda1");
        agenda.setDescription("Description1");

        CreateAgendaDTO agendaDTO = new CreateAgendaDTO("Agenda1", "Description1");

        given(agendaService.createAgenda(agendaDTO)).willReturn(agenda);

        mockMvc.perform(post("/api/agendas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(agendaDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("Agenda1"))
                .andExpect(jsonPath("$.description").value("Description1"))
                .andExpect(jsonPath("$.openedDate").doesNotExist())
                .andExpect(jsonPath("$.limitDate").doesNotExist());

        verify(agendaService).createAgenda(agendaDTO);
    }

    @Test
    @DisplayName("should find all agendas")
    public void shouldFindAllAgendas() throws Exception {
        LocalDateTime dateTime = LocalDateTime.of(
                2026, 7, 31, 20, 20, 27, 0
        );

        AgendaVotingProjection projection1 = new AgendaVotingProjection.Builder(1L).setTitle("Agenda1").setDescription("Description1").build();
        AgendaVotingProjection projection2 = new AgendaVotingProjection.Builder(2L).setTitle("Agenda2").setDescription("").build();
        AgendaVotingProjection projection3 = new AgendaVotingProjection.Builder(3L).setTitle("Agenda3").setDescription("").setOpenedDate(dateTime).build();
        AgendaVotingProjection projection4 = new AgendaVotingProjection.Builder(4L).setTitle("Agenda4").setDescription("").setOpenedDate(dateTime).setLimitDate(dateTime).build();
        AgendaVotingProjection projection5 = new AgendaVotingProjection.Builder(5L).setTitle("Agenda5").setDescription("").setOpenedDate(dateTime).setLimitDate(dateTime).setYesVotes(5).setTotalVotes(5).build();
        AgendaVotingProjection projection6 = new AgendaVotingProjection.Builder(6L).setTitle("Agenda5").setDescription("").setOpenedDate(dateTime).setLimitDate(dateTime).setYesVotes(5).setNoVotes(5).setTotalVotes(10).build();

        List<AgendaVotingProjection> projectionList = List.of(
                projection1,
                projection2,
                projection3,
                projection4,
                projection5,
                projection6
        );

        given(agendaService.findAllAgendaVotingProjection()).willReturn(projectionList);

        mockMvc.perform(get("/api/agendas"))
                .andExpect(status().isOk())

                .andExpect(jsonPath("$.length()").value(6))

                // Agenda 1 - PENDING, no votes
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Agenda1"))
                .andExpect(jsonPath("$[0].description").value("Description1"))
                .andExpect(jsonPath("$[0].status").value("PENDING"))
                .andExpect(jsonPath("$[0].yesVotes").value(0))
                .andExpect(jsonPath("$[0].noVotes").value(0))
                .andExpect(jsonPath("$[0].totalVotes").value(0))

                // Agenda 2 - PENDING, no dates
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].title").value("Agenda2"))
                .andExpect(jsonPath("$[1].description").value(""))
                .andExpect(jsonPath("$[1].status").value("PENDING"))
                .andExpect(jsonPath("$[1].yesVotes").value(0))
                .andExpect(jsonPath("$[1].noVotes").value(0))
                .andExpect(jsonPath("$[1].totalVotes").value(0))

                // Agenda 3 - PENDING, opened but no limit date
                .andExpect(jsonPath("$[2].id").value(3))
                .andExpect(jsonPath("$[2].title").value("Agenda3"))
                .andExpect(jsonPath("$[2].description").value(""))
                .andExpect(jsonPath("$[2].status").value("PENDING"))
                .andExpect(jsonPath("$[2].yesVotes").value(0))
                .andExpect(jsonPath("$[2].noVotes").value(0))
                .andExpect(jsonPath("$[2].totalVotes").value(0))

                // Agenda 4 - FINISHED, no votes
                .andExpect(jsonPath("$[3].id").value(4))
                .andExpect(jsonPath("$[3].title").value("Agenda4"))
                .andExpect(jsonPath("$[3].description").value(""))
                .andExpect(jsonPath("$[3].status").value("FINISHED"))
                .andExpect(jsonPath("$[3].yesVotes").value(0))
                .andExpect(jsonPath("$[3].noVotes").value(0))
                .andExpect(jsonPath("$[3].totalVotes").value(0))

                // Agenda 5 - FINISHED, only YES votes
                .andExpect(jsonPath("$[4].id").value(5))
                .andExpect(jsonPath("$[4].title").value("Agenda5"))
                .andExpect(jsonPath("$[4].description").value(""))
                .andExpect(jsonPath("$[4].status").value("FINISHED"))
                .andExpect(jsonPath("$[4].yesVotes").value(5))
                .andExpect(jsonPath("$[4].noVotes").value(0))
                .andExpect(jsonPath("$[4].totalVotes").value(5))

                // Agenda 6 - FINISHED, YES + NO votes
                .andExpect(jsonPath("$[5].id").value(6))
                .andExpect(jsonPath("$[5].title").value("Agenda5"))
                .andExpect(jsonPath("$[5].description").value(""))
                .andExpect(jsonPath("$[5].status").value("FINISHED"))
                .andExpect(jsonPath("$[5].yesVotes").value(5))
                .andExpect(jsonPath("$[5].noVotes").value(5))
                .andExpect(jsonPath("$[5].totalVotes").value(10));

        verify(agendaService).findAllAgendaVotingProjection();
    }

    @Test
    @DisplayName("should not create an agenda with invalid properties")
    public void shouldNotCreateAgenda() throws Exception {
        CreateAgendaDTO nullTitleDTO = new CreateAgendaDTO(null, "");
        CreateAgendaDTO emptyTitleDTO = new CreateAgendaDTO("", "");
        CreateAgendaDTO invalidMinLengthTitleDTO = new CreateAgendaDTO("1", "");
        CreateAgendaDTO invalidMaxLengthTitleDTO = new CreateAgendaDTO("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789ABCDEFGH this is a > 60 characters length string", "");
        CreateAgendaDTO nullDescriptionDTO = new CreateAgendaDTO("Agenda", null);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/agendas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nullTitleDTO)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.title").value("O título deve estar preenchido"));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/agendas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(emptyTitleDTO)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.title").value("O título deve ser maior que 2 e não exceder 120 caracteres"));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/agendas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidMinLengthTitleDTO)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.title").value("O título deve ser maior que 2 e não exceder 120 caracteres"));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/agendas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidMaxLengthTitleDTO)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.title").value("O título deve ser maior que 2 e não exceder 120 caracteres"));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/agendas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nullDescriptionDTO)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.description").value("A descrição não pode ser nula"));

        verify(agendaService, times(0)).createAgenda(any());
    }

    @Test
    @DisplayName("Should open an agenda session")
    void shouldOpenAgendaSession() throws Exception {
        Long agendaId = 1L;

        LocalDateTime limitDate = LocalDateTime.of(2026, 7, 30, 20, 0);

        OpenSessionDTO openSessionDTO = new OpenSessionDTO(30);

        when(agendaService.openAgendaAndReturnLimitDate(eq(agendaId), eq(30))).thenReturn(limitDate);

        mockMvc.perform(post("/api/agendas/{agendaId}/open", agendaId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(openSessionDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.limitDate").exists());

        verify(agendaService).openAgendaAndReturnLimitDate(eq(agendaId), eq(30));
    }

    @Test
    @DisplayName("Should return bad request when duration is null")
    void shouldNotOpenSessionWithInvalidDuration() throws Exception {
        OpenSessionDTO duration1 = new OpenSessionDTO(0);
        OpenSessionDTO duration2 = new OpenSessionDTO(-50);
        OpenSessionDTO duration3 = new OpenSessionDTO(null);

        mockMvc.perform(post("/api/agendas/{agendaId}/open", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(duration1)))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/api/agendas/{agendaId}/open", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(duration2)))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/api/agendas/{agendaId}/open", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(duration3)))
                .andExpect(status().isBadRequest());

        verify(agendaService, times(0)).openAgendaAndReturnLimitDate(any(), any());
    }

    @Test
    @DisplayName("Should vote successfully")
    void shouldVoteSuccessfully() throws Exception {
        VoteDTO voteDTO = new VoteDTO("123456789", VoteOption.YES);

        mockMvc.perform(post("/api/agendas/1/vote")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(voteDTO)))
                .andExpect(status().isOk());

        verify(agendaService).voteOnAgenda(eq(1L), any(VoteDTO.class));
    }

    @Test
    @DisplayName("Should return error when associateId is invalid")
    void shouldReturnBadRequestWhenAssociateIdIsInvalid() throws Exception {
        VoteDTO associateBlankDTO = new VoteDTO("", VoteOption.YES);
        VoteDTO associateNullDTO = new VoteDTO(null, VoteOption.YES);

        mockMvc.perform(post("/api/agendas/1/vote")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(associateBlankDTO)))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/api/agendas/1/vote")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(associateNullDTO)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(agendaService);
    }

    @Test
    @DisplayName("Should return exception when vote option is invalid")
    void shouldReturnBadRequestWhenVoteOptionIsInvalid() throws Exception {
        VoteDTO invalidVoteOption = new VoteDTO("1234", null);

        mockMvc.perform(post("/api/agendas/1/vote")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(invalidVoteOption)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(agendaService);
    }
}
