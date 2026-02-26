package db.server.app.domain.User.services;

import db.server.app.domain.User.Services.ByEncryptService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(MockitoExtension.class)
public class ByEncryptServiceTest {

    @InjectMocks
    private ByEncryptService byEncryptService;

    @Test
    void checkStringEncodeAndIfThiMatch() {
        String pass = "teste";
        String response = byEncryptService.encode(pass);

        Boolean result = byEncryptService.matches(pass, response);
        assertEquals(true, result);
    }
}
