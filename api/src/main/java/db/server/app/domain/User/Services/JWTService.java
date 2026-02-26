package db.server.app.domain.User.Services;

import db.server.app.commons.Helpers.JWT.SignKey;
import db.server.app.domain.User.Model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.function.Function;


@Service
public class JWTService {
    private final SignKey sk;

    public JWTService(SignKey sk) {
        this.sk = sk;
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(sk.getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String extractSubject(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean isTokenValid(String token, UserDetails user) {
        final String username = extractSubject(token);
        return username.equals(user.getUsername());
    }
}
