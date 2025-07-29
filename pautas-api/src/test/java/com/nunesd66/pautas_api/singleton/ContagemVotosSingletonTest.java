package com.nunesd66.pautas_api.singleton;

import com.nunesd66.pautas_api.voto.model.Voto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ContagemVotosSingletonTest {

    private ContagemVotosSingleton singleton1;
    private ContagemVotosSingleton singleton2;

    @BeforeEach
    void setup() {
        singleton1 = ContagemVotosSingleton.getInstancia();
        singleton1.resetarVotos();
        singleton2 = ContagemVotosSingleton.getInstancia();
    }

    @Test
    void getInstancia_shouldReturnSameInstance() {
        assertSame(singleton1, singleton2,
                "getInstancia deve retornar a mesma instância do singleton");
    }

    @Test
    void adicionar_and_hasVotoByCpf_workCorrectly() {
        Voto voto = new Voto();
        voto.setCpfAssociado("11122233344");

        assertFalse(singleton1.hasVotoByCpf("11122233344"),
                "Não deve ter voto antes de adicionar");

        singleton1.adicionar(voto);
        assertTrue(singleton1.hasVotoByCpf("11122233344"),
                "Deve ter voto após adicionar");

        assertTrue(singleton2.hasVotoByCpf("11122233344"),
                "A segunda referência deve ver o voto adicionado");
    }

    @Test
    void resetarVotos_clearsAllVotes() {
        Voto voto = new Voto();
        voto.setCpfAssociado("55566677788");

        singleton1.adicionar(voto);
        assertTrue(singleton1.hasVotoByCpf("55566677788"));

        singleton1.resetarVotos();
        assertFalse(singleton1.hasVotoByCpf("55566677788"),
                "Não deve ter voto após resetar votos");
    }
}
