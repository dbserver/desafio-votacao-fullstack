package db.server.app.domain.User.services;

import db.server.app.domain.User.Model.User;
import db.server.app.domain.User.Repository.UserRepository;
import db.server.app.domain.User.Services.UserDetailsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsService userDetailsService;

    @Test
    void checkIfGetUserDetailsWork() {
        User user = new User();
        user.setId("123");
        user.setName("name");
        user.setEmail("email");
        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.of(user));

        UserDetails details = new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                null,
                List.of()
        );

        UserDetails response = userDetailsService.loadUserByEmail(user.getEmail());
        assertEquals(details, response);
    }

}
