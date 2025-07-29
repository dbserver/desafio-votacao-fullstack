package com.nunesd66.pautas_api.pauta;

import com.nunesd66.pautas_api.pauta.exception.PautaException;
import com.nunesd66.pautas_api.pauta.model.Pauta;
import com.nunesd66.pautas_api.pauta.repository.PautaRepository;
import com.nunesd66.pautas_api.pauta.validator.PautaValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PautaValidatorTest {

    @Mock
    private PautaRepository repository;

    @InjectMocks
    private PautaValidator validator;

    @BeforeEach
    void setup() {
    }


    @Test
    void quandoNaoExistirPautaEmAndamento_naoDeveLancarExcecao() {
        Pauta nova = new Pauta();
        nova.setTitulo("Título Único");

        when(repository.getLastRecord()).thenReturn(Optional.empty());
        when(repository.findByTitulo("Título Único")).thenReturn(null);

        assertDoesNotThrow(() -> validator.validCreatePauta(nova));

        verify(repository).getLastRecord();
        verify(repository).findByTitulo("Título Único");
    }


    @Test
    void quandoExistirPautaEmAndamento_deveLancarPautaException() {
        Pauta existente = new Pauta();
        when(repository.getLastRecord()).thenReturn(Optional.of(existente));

        Pauta nova = new Pauta();
        nova.setTitulo("Qualquer");

        PautaException ex = assertThrows(PautaException.class,
                () -> validator.validCreatePauta(nova)
        );
        assertEquals("Já existe uma pauta em andamento.", ex.getMessage());

        verify(repository).getLastRecord();
        verify(repository, never()).findByTitulo(any());
    }

    @Test
    void quandoTituloJaExistir_deveLancarPautaException() {
        Pauta nova = new Pauta();
        nova.setTitulo("Duplicado");

        when(repository.getLastRecord()).thenReturn(Optional.empty());
        when(repository.findByTitulo("Duplicado")).thenReturn(new Pauta());

        PautaException ex = assertThrows(PautaException.class,
                () -> validator.validCreatePauta(nova)
        );
        assertEquals("Já existe uma pauta com esse título.", ex.getMessage());

        verify(repository).getLastRecord();
        verify(repository).findByTitulo("Duplicado");
    }
}
