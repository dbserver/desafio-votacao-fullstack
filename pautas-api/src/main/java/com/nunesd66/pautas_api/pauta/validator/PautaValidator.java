package com.nunesd66.pautas_api.pauta.validator;

import com.nunesd66.pautas_api.pauta.model.Pauta;
import com.nunesd66.pautas_api.pauta.exception.PautaException;
import com.nunesd66.pautas_api.pauta.repository.PautaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PautaValidator {

    private final PautaRepository repository;

    public void validCreatePauta(Pauta pauta) throws PautaException {
        Pauta pautaLastRecord = this.repository.getLastRecord().orElse(null);

        if (pautaLastRecord != null) {
            throw new PautaException("Já existe uma pauta em andamento.");
        }

        Pauta pautaByTitulo = this.repository.findByTitulo(pauta.getTitulo());
        if (pautaByTitulo != null) {
            throw new PautaException("Já existe uma pauta com esse título.");
        }
    }

}
