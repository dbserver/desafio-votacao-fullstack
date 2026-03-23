package br.tec.db.voting_api.external.cpf;

import br.tec.db.voting_api.exception.BusinessException;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class CpfClientFake implements  CpfClient {

    private final Random random = new Random();

    @Override
    public CpfResponse validateCPF(String cpf) {

        boolean ValidCPF = random.nextBoolean();

        if (ValidCPF) {
            return new CpfResponse(CpfStatus.ABLE_TO_VOTE);
        }

        return new CpfResponse(CpfStatus.UNABLE_TO_VOTE);
    }
}
