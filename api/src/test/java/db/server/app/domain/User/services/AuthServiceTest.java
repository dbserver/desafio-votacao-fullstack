package db.server.app.domain.User.services;

import db.server.app.domain.User.Model.User;
import db.server.app.domain.User.Services.AuthService;
import db.server.app.domain.User.Services.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private Authentication authentication;

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setup() {
        SecurityContextHolder.clearContext();
    }

    @AfterEach
    void cleanup() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldReturnSessionUser() throws Exception {
        String email = "user@email.com";

        User user = new User();
        user.setEmail(email);

        when(userDetails.getUsername()).thenReturn(email);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userService.getUserByEmail(email))
                .thenReturn(Optional.of(user));

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        User result = authService.getSessionUser();

        assertEquals(email, result.getEmail());
    }
}