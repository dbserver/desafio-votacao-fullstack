package db.server.app.domain.User.services;

import db.server.app.domain.User.DTO.UserDTO;
import db.server.app.domain.User.Model.User;
import db.server.app.domain.User.Repository.UserRepository;
import db.server.app.domain.User.Services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void checkHealth() {
        String response = userService.getHealth();
        assertEquals("Healthy", response);
    }

    @Test
    void checkReturnWhenANewUserIsCreated() throws Exception {
        User newUser = new User();
        newUser.setId("123");
        newUser.setName("name");
        newUser.setEmail("email");

        when(userRepository.save(any(User.class))).thenReturn(newUser);

        UserDTO userDTO = new UserDTO(null, "name", "email");

        UserDTO response = userService.save(userDTO);
        UserDTO dto = new UserDTO(Optional.of(newUser.getId()), newUser.getName(), newUser.getEmail());
        assertEquals(dto, response);
    }

    @Test
    void checkReturnOfUserByEmail() {
        User user = new User();
        user.setId("123");
        user.setName("name");
        user.setEmail("email");

        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.of(user));

        Optional<User> response = userService.getUserByEmail("email");
        assertEquals(Optional.of(user), response);
    }
}
