package db.server.app.domain.Rulling.DTO;

import java.util.Optional;

public record NewRullingDTO(
        String title,
        String description,
        Optional<String> expiration
) {
}
