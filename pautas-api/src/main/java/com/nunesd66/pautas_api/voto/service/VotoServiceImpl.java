package com.nunesd66.pautas_api.voto.service;

import com.nunesd66.pautas_api.pauta.dto.PautaResponse;
import com.nunesd66.pautas_api.pauta.model.Pauta;
import com.nunesd66.pautas_api.pauta.service.PautaService;
import com.nunesd66.pautas_api.singleton.ContagemVotosSingleton;
import com.nunesd66.pautas_api.voto.validator.VotoValidator;
import com.nunesd66.pautas_api.voto.exception.VotoException;
import com.nunesd66.pautas_api.voto.model.Voto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class VotoServiceImpl implements VotoService {

    private final PautaService pautaService;
    private final VotoValidator validator;

    @Override
    public void votar(Voto voto) throws VotoException {
        PautaResponse pauta = this.pautaService.getLastRecord();
        ContagemVotosSingleton singleton = ContagemVotosSingleton.getInstancia();
        this.validator.validVotar(pauta, voto, singleton);
        singleton.adicionar(voto);
    }

}
