package com.votacao.backend.infrastructure.cpf;

import com.votacao.backend.infrastructure.exception.CpfInvalidoException;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
@Profile("!test")
public class FakeCpfValidationClient implements CpfValidationClient {

    private final Random random;

    public FakeCpfValidationClient() {
        this(new Random());
    }

    FakeCpfValidationClient(Random random) {
        this.random = random;
    }

    @Override
    public CpfStatus validar(String cpf) {
        if (cpf == null || cpf.isBlank()) {
            throw new CpfInvalidoException();
        }

        int sorteio = random.nextInt(4);
        if (sorteio == 0) {
            throw new CpfInvalidoException();
        }

        return sorteio % 2 == 0 ? CpfStatus.UNABLE_TO_VOTE : CpfStatus.ABLE_TO_VOTE;
    }
}
