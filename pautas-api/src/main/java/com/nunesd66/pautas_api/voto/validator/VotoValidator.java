package com.nunesd66.pautas_api.voto.validator;

import com.nunesd66.pautas_api.pauta.dto.PautaResponse;
import com.nunesd66.pautas_api.pauta.model.Pauta;
import com.nunesd66.pautas_api.singleton.ContagemVotosSingleton;
import com.nunesd66.pautas_api.voto.exception.VotoException;
import com.nunesd66.pautas_api.voto.model.Voto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class VotoValidator {

    public void validVotar(PautaResponse pauta, Voto voto, ContagemVotosSingleton singleton) throws VotoException {
        if (pauta != null) {
            boolean podeVotar = !singleton.hasVotoByCpf(voto.getCpfAssociado());
            if (podeVotar) {
                return;
            }

            throw new VotoException("Esse CPF já votou.");
        }

        throw new VotoException("Não consta nenhuma pauta aberta para essa votação no momento.");
    }

}
