package db.server.app.domain.User.Interfaces;

public interface IPassEncoder {
    String encode(String password);
    boolean matches(String password, String encodedPassword);
}
