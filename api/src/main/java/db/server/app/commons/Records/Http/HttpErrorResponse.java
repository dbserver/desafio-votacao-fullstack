package db.server.app.commons.Records.Http;

import java.util.Optional;

public record HttpErrorResponse(
        int status,
        Optional<String> message
) {
}
