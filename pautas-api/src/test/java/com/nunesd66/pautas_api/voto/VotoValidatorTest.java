package com.nunesd66.pautas_api.voto;

import com.nunesd66.pautas_api.pauta.dto.PautaResponse;
import com.nunesd66.pautas_api.singleton.ContagemVotosSingleton;
import com.nunesd66.pautas_api.voto.exception.VotoException;
import com.nunesd66.pautas_api.voto.model.Voto;
import com.nunesd66.pautas_api.voto.validator.VotoValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VotoValidatorTest {

    private VotoValidator validator;

    @Mock
    private ContagemVotosSingleton singleton;

    @BeforeEach
    void setup() {
        validator = new VotoValidator();
    }

    @Test
    void quandoPautaNull_deveLancarVotoException() {
        Voto voto = new Voto();
        voto.setCpfAssociado("123");

        VotoException ex = assertThrows(VotoException.class,
                () -> validator.validVotar(null, voto, singleton)
        );
        assertEquals("Não consta nenhuma pauta aberta para essa votação no momento.", ex.getMessage());
    }

    @Test
    void quandoCpfNaoVotou_naoDeveLancar() {
        PautaResponse pauta = new PautaResponse(1L, "T", "D", null);
        Voto voto = new Voto();
        voto.setCpfAssociado("123");

        when(singleton.hasVotoByCpf("123")).thenReturn(false);

        assertDoesNotThrow(() -> validator.validVotar(pauta, voto, singleton));
    }

    @Test
    void quandoCpfJaVotou_deveLancarVotoException() {
        PautaResponse pauta = new PautaResponse(1L, "T", "D", null);
        Voto voto = new Voto();
        voto.setCpfAssociado("123");

        when(singleton.hasVotoByCpf("123")).thenReturn(true);

        VotoException ex = assertThrows(VotoException.class,
                () -> validator.validVotar(pauta, voto, singleton)
        );
        assertEquals("Esse CPF já votou.", ex.getMessage());
    }
}
