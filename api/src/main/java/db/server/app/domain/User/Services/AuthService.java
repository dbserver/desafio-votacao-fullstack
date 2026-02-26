package db.server.app.domain.User.Services;

import db.server.app.domain.User.Model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    private final UserService userService;

    public AuthService(UserService userService) {
        this.userService = userService;
    }

    public User getSessionUser() throws Exception {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) auth.getPrincipal();

        Optional<User> user = userService.getUserByEmail(userDetails.getUsername());
        if (user.isEmpty()) {
            throw new Exception("User not found");
        }

        return user.get();
    }

}
