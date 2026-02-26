package db.server.app.domain.Rulling.DTO;

import java.util.Date;

public record RullingDTO(
        String title,
        String description,
        String author,
        Date expiration
) {
}
