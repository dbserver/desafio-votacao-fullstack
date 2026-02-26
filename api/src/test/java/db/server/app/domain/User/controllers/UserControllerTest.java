package db.server.app.domain.User.controllers;

import db.server.app.commons.App.UseCases.AuthUserUseCase;
import db.server.app.domain.User.Controllers.UserController;
import db.server.app.domain.User.DTO.AuthResponse;
import db.server.app.domain.User.DTO.LoginCommand;
import db.server.app.domain.User.DTO.LoginDTO;
import db.server.app.domain.User.DTO.UserDTO;
import db.server.app.domain.User.Services.JWTService;
import db.server.app.domain.User.Services.UserDetailsService;
import db.server.app.domain.User.Services.UserService;
import db.server.app.infra.security.JwtAuthFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import tools.jackson.databind.ObjectMapper;

import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(UserController.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private AuthUserUseCase authUserUseCase;

    @MockitoBean
    private JWTService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void checkHealthRequest() throws Exception {
        String response = "Healthy";

        when(userService.getHealth()).thenReturn(response);

        mockMvc.perform(get("/user/health"))
                .andExpect(status().isOk())
                .andExpect(content().string(response));
    }

    @Test
    void checkCreateUserRequest() throws Exception {
        when(userService.save(any(UserDTO.class))).thenReturn(new UserDTO(Optional.of("123"), "name", "email"));

        mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new UserDTO(null, "name", "email"))))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(new UserDTO(Optional.of("123"), "name", "email"))));
    }

    @Test
    void checkLoginRequest() throws Exception {
        AuthResponse response = new AuthResponse("token");
        LoginDTO request = new LoginDTO("user@email.com");

        when(authUserUseCase.execute(any(LoginCommand.class))).thenReturn(response);

        mockMvc.perform(post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(response)));
    }

    @Test
    void checkLoginFailureRequest() throws Exception {
        LoginDTO request = new LoginDTO("user@email.com");

        when(authUserUseCase.execute(any(LoginCommand.class))).thenThrow(new RuntimeException("Invalid credentials"));
        mockMvc.perform(post("/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());

    }

}
