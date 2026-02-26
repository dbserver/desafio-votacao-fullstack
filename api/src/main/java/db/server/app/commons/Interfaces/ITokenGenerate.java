package db.server.app.commons.Interfaces;

import db.server.app.domain.User.Model.User;

public interface ITokenGenerate {
    public String generate(User user);
}
