package com.nunesd66.pautas_api.voto;

import com.nunesd66.pautas_api.pauta.dto.PautaResponse;
import com.nunesd66.pautas_api.pauta.service.PautaService;
import com.nunesd66.pautas_api.singleton.ContagemVotosSingleton;
import com.nunesd66.pautas_api.voto.exception.VotoException;
import com.nunesd66.pautas_api.voto.model.Voto;
import com.nunesd66.pautas_api.voto.service.VotoServiceImpl;
import com.nunesd66.pautas_api.voto.validator.VotoValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VotoServiceImplTest {

    @Mock
    private PautaService pautaService;

    @Mock
    private VotoValidator validator;

    @InjectMocks
    private VotoServiceImpl service;

    @BeforeEach
    void setup() {
        ContagemVotosSingleton.getInstancia().resetarVotos();
    }

    @Test
    void votar_semPautaAberta_throwsVotoException() throws VotoException {
        Voto voto = new Voto();
        voto.setCpfAssociado("123");
        when(pautaService.getLastRecord()).thenReturn(null);
        doThrow(new VotoException("Não consta nenhuma pauta aberta para essa votação no momento."))
                .when(validator).validVotar(isNull(), eq(voto), any(ContagemVotosSingleton.class));

        VotoException ex = assertThrows(VotoException.class,
                () -> service.votar(voto)
        );
        assertEquals(
                "Não consta nenhuma pauta aberta para essa votação no momento.",
                ex.getMessage()
        );
        assertFalse(ContagemVotosSingleton.getInstancia().hasVotoByCpf("123"));
    }

    @Test
    void votar_valido_addsVoteToSingleton() throws VotoException {
        Voto voto = new Voto();
        voto.setCpfAssociado("abc");
        PautaResponse pauta = new PautaResponse(1L, "T", "D", null);
        when(pautaService.getLastRecord()).thenReturn(pauta);

        service.votar(voto);

        assertTrue(ContagemVotosSingleton.getInstancia().hasVotoByCpf("abc"));
    }

    @Test
    void votar_validatorThrows_doesNotAddVote() throws VotoException {
        Voto voto = new Voto();
        voto.setCpfAssociado("xyz");
        PautaResponse pauta = new PautaResponse(1L, "T", "D", null);
        when(pautaService.getLastRecord()).thenReturn(pauta);
        doThrow(new VotoException("err"))
                .when(validator).validVotar(eq(pauta), eq(voto), any(ContagemVotosSingleton.class));

        VotoException ex = assertThrows(VotoException.class,
                () -> service.votar(voto)
        );
        assertEquals("err", ex.getMessage());
        assertFalse(ContagemVotosSingleton.getInstancia().hasVotoByCpf("xyz"));
    }

}
