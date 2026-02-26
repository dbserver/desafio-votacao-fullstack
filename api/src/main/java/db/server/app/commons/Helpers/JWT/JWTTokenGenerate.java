package db.server.app.commons.Helpers.JWT;

import db.server.app.commons.Interfaces.ITokenGenerate;
import db.server.app.domain.User.Model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

@Component
public class JWTTokenGenerate implements ITokenGenerate {
    private final SignKey signKey;

    public JWTTokenGenerate(SignKey signKey) {
        this.signKey = signKey;
    }

    @Override
    public String generate(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .signWith(signKey.getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }
}
