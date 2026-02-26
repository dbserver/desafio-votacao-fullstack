package db.server.app.domain.Rulling.controllers;

import db.server.app.commons.Records.Http.HttpErrorResponse;
import db.server.app.domain.Rulling.Controllers.RullingController;
import db.server.app.domain.Rulling.DTO.RullingCountDTO;
import db.server.app.domain.Rulling.DTO.RullingDTO;
import db.server.app.domain.Rulling.Model.Rulling;
import db.server.app.domain.Rulling.Services.RullingService;
import db.server.app.domain.User.Model.User;
import db.server.app.domain.User.Services.AuthService;
import db.server.app.domain.User.Services.JWTService;
import db.server.app.domain.User.Services.UserDetailsService;
import db.server.app.infra.security.JwtAuthFilter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(RullingController.class)
@AutoConfigureMockMvc(addFilters = false)
public class RullingControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RullingService rullingService;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private JWTService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @MockitoBean
    private JwtAuthFilter jwtAuthFilter;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() throws Exception {
        User user = new User();
        user.setId("123");
        user.setName("name");
        user.setEmail("email@gmail.com");

        when(authService.getSessionUser()).thenReturn(user);
    }

    @Test
    void checkCreateRulling() throws Exception {
        Rulling newRulling = new Rulling();
        Date now = new Date();
        now.setTime(now.getTime() + 1000 * 60);
        newRulling.setNewRulling("Title", "Description", "123", now);
        when(rullingService.save(any(RullingDTO.class))).thenReturn(Optional.of(newRulling));


        mockMvc.perform(post("/rulling")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new RullingDTO("Title", "Description", "123", now))))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(newRulling)));
    }

    @Test
    void checkVoteYesRequest() throws Exception {
        Rulling rulling = new Rulling();
        when(rullingService.userAlreadyVoted(any(String.class), any(String.class))).thenReturn(false);
        when(rullingService.addVoteYes(any(String.class), any(String.class))).thenReturn(Optional.of(rulling));
        mockMvc.perform(patch("/rulling/123/vote/yes"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(rulling)));
    }

    @Test
    void checkVoteNoRequest() throws Exception {
        Rulling rulling = new Rulling();
        when(rullingService.userAlreadyVoted(any(String.class), any(String.class))).thenReturn(false);
        when(rullingService.addVoteNo(any(String.class), any(String.class))).thenReturn(Optional.of(rulling));
        mockMvc.perform(patch("/rulling/123/vote/no"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(rulling)));
    }

    @Test
    void checkVoteNoInativeRequest() throws Exception {
        HttpErrorResponse response = new HttpErrorResponse(HttpStatus.CONFLICT.value(), Optional.of("You already voted"));
        Rulling rulling = new Rulling();
        when(rullingService.userAlreadyVoted(any(String.class), any(String.class))).thenReturn(true);
        when(rullingService.addVoteNo(any(String.class), any(String.class))).thenReturn(Optional.of(rulling));
        mockMvc.perform(patch("/rulling/123/vote/no"))
                .andExpect(status().isConflict())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    void checkVoteYesInativeRequest() throws Exception {
        HttpErrorResponse response = new HttpErrorResponse(HttpStatus.CONFLICT.value(), Optional.of("You already voted"));
        Rulling rulling = new Rulling();
        when(rullingService.userAlreadyVoted(any(String.class), any(String.class))).thenReturn(true);
        when(rullingService.addVoteYes(any(String.class), any(String.class))).thenReturn(Optional.of(rulling));
        mockMvc.perform(patch("/rulling/123/vote/yes"))
                .andExpect(status().isConflict())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    void checkRullingInfo() throws Exception {
        when(rullingService.countRullings()).thenReturn(100L);
        double pages = Math.ceil((double) 100L / 24);
        RullingCountDTO response = new RullingCountDTO(
                100L,
                (long) pages
        );

        mockMvc.perform(get("/rulling/navigate/info"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    void checkRullingsByPage() throws Exception {
        Rulling rulling = new Rulling();
        List<Rulling> rullings = List.of(rulling);
        when(rullingService.getRullings(anyLong(), anyLong())).thenReturn(rullings);

        mockMvc.perform(get("/rulling/all/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(rullings)));
    }
}
