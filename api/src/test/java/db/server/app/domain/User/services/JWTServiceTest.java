package db.server.app.domain.User.services;

import db.server.app.commons.Helpers.JWT.SignKey;
import db.server.app.domain.User.Services.JWTService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Key;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JWTServiceTest {

    @Mock
    private SignKey signKey;

    @Mock
    private UserDetails userDetails;

    private JWTService jwtService;

    private Key key;

    @BeforeEach
    void setup() {
        key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

        when(signKey.getSignKey()).thenReturn(key);

        jwtService = new JWTService(signKey);
    }

    private String generateToken(String subject) {
        return Jwts.builder()
                .setSubject(subject)
                .signWith(key)
                .compact();
    }

    @Test
    void shouldExtractSubject() {
        String token = generateToken("user");

        String subject = jwtService.extractSubject(token);

        assertEquals("user", subject);
    }

    @Test
    void shouldValidateTokenWhenUsernameMatches() {
        String token = generateToken("user");

        when(userDetails.getUsername()).thenReturn("user");

        boolean valid = jwtService.isTokenValid(token, userDetails);

        assertTrue(valid);
    }

    @Test
    void shouldInvalidateTokenWhenUsernameDoesNotMatch() {
        String token = generateToken("user");

        when(userDetails.getUsername()).thenReturn("anotherUser");

        boolean valid = jwtService.isTokenValid(token, userDetails);

        assertFalse(valid);
    }

    @Test
    void shouldThrowExceptionForInvalidToken() {
        assertThrows(Exception.class, () ->
                jwtService.extractSubject("5494c2dbb9539bdd2d5a65dfc14157529432137ed781cd75421c4fc36c8dc5f5")
        );
    }
}