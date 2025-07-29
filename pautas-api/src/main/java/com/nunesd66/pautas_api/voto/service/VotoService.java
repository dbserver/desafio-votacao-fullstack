package com.nunesd66.pautas_api.voto.service;

import com.nunesd66.pautas_api.voto.exception.VotoException;
import com.nunesd66.pautas_api.voto.model.Voto;

public interface VotoService {
    void votar(Voto voto) throws VotoException;
}
