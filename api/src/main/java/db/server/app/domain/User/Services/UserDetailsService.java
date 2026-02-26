package db.server.app.domain.User.Services;

import db.server.app.domain.User.Interfaces.IUserDetailsService;
import db.server.app.domain.User.Model.User;
import db.server.app.domain.User.Repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.NoSuchElementException;

@Component
public class UserDetailsService implements IUserDetailsService {
    private final UserRepository userRepository;

    public UserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByEmail(String email) throws NoSuchElementException {
        User user = this.userRepository.findByEmail(email).orElseThrow();

        return new org.springframework.security.core.userdetails.User(
            user.getEmail(),
            null,
            List.of()
        );
    }
}
