package db.server.app.domain.User.Services;

import db.server.app.domain.User.Interfaces.IPassEncoder;
import db.server.app.domain.User.DTO.UserDTO;
import db.server.app.domain.User.Model.User;
import db.server.app.domain.User.Repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository, IPassEncoder passEncoder) {
        this.userRepository = userRepository;
    }

    public String getHealth() {
        return "Healthy";
    }

    public UserDTO save(UserDTO user) throws Exception {
        User newUser = new User();
        newUser.setNewUser(user.name(), user.email());

        newUser = this.userRepository.save(newUser);

        return new UserDTO(Optional.of(newUser.getId()), newUser.getName(), newUser.getEmail());
    }

    public Optional<User> getUserByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }
}
