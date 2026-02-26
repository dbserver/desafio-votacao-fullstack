package db.server.app.domain.User.Interfaces;

import org.springframework.security.core.userdetails.UserDetails;

public interface IUserDetailsService {
    public UserDetails loadUserByEmail(String email);
}
