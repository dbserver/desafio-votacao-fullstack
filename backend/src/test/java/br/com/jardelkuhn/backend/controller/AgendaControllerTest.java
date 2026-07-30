package br.com.jardelkuhn.backend.controller;

import br.com.jardelkuhn.backend.dto.CreateAgendaDTO;
import br.com.jardelkuhn.backend.entity.Agenda;
import br.com.jardelkuhn.backend.service.AgendaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
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
        Agenda agenda = new Agenda.Builder().setTitle("Agenda1").setDescription("Description1").build();

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
    }

    @Test
    @DisplayName("should find all agendas")
    public void shouldFindAllAgendas() throws Exception {
        LocalDateTime dateTime = LocalDateTime.now();

        Agenda agenda1 = new Agenda.Builder().setTitle("Agenda1").setDescription("Description1").build();
        Agenda agenda2 = new Agenda.Builder().setTitle("Agenda2").setDescription("").build();
        Agenda agenda3 = new Agenda.Builder().setTitle("Agenda3").setDescription("").setOpenedDate(dateTime).build();
        Agenda agenda4 = new Agenda.Builder().setTitle("Agenda4").setDescription("").setOpenedDate(dateTime).setLimitDate(dateTime).build();

        List<Agenda> agendaList = List.of(agenda1, agenda2, agenda3, agenda4);

        given(agendaService.listAllAgendas()).willReturn(agendaList);

        mockMvc.perform(get("/api/agendas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4))

                // Agenda 1
                .andExpect(jsonPath("$[0].title").value("Agenda1"))
                .andExpect(jsonPath("$[0].description").value("Description1"))
                .andExpect(jsonPath("$[0].openedDate").doesNotExist())
                .andExpect(jsonPath("$[0].limitDate").doesNotExist())

                // Agenda 2
                .andExpect(jsonPath("$[1].title").value("Agenda2"))
                .andExpect(jsonPath("$[1].description").value(""))
                .andExpect(jsonPath("$[1].openedDate").doesNotExist())
                .andExpect(jsonPath("$[1].limitDate").doesNotExist())

                // Agenda 3
                .andExpect(jsonPath("$[2].title").value("Agenda3"))
                .andExpect(jsonPath("$[2].description").value(""))
                .andExpect(jsonPath("$[2].openedDate").value(dateTime.toString()))
                .andExpect(jsonPath("$[2].limitDate").doesNotExist())

                // Agenda 4
                .andExpect(jsonPath("$[3].title").value("Agenda4"))
                .andExpect(jsonPath("$[3].description").value(""))
                .andExpect(jsonPath("$[3].openedDate").value(dateTime.toString()))
                .andExpect(jsonPath("$[3].limitDate").value(dateTime.toString()));
    }


    @Test
    @DisplayName("should not create an agenda with invalid properties")
    public void shouldNotCreateGame() throws Exception {
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
    }
}
