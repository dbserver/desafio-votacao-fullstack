package db.server.app.domain.User.Helpers.Exceptions;

public class InvalidUser extends RuntimeException {
    public InvalidUser(String email) {
        super("User with email: " + email + " not found!");
    }
}
