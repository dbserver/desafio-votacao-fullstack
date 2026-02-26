package db.server.app.domain.User.DTO;

import java.util.Optional;

public record UserDTO(
        Optional<String> id,
        String name,
        String email
) {}
