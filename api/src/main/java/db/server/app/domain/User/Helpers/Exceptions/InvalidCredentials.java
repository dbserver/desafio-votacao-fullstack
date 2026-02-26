package db.server.app.domain.User.Helpers.Exceptions;

public class InvalidCredentials extends RuntimeException {
    public InvalidCredentials() {
        super("Invalid credentials!");
    }
}
