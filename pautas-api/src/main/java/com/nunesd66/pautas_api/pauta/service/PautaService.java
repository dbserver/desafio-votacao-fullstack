package com.nunesd66.pautas_api.pauta.service;

import com.nunesd66.pautas_api.pauta.dto.PautaCreateRequest;
import com.nunesd66.pautas_api.pauta.dto.PautaResponse;
import com.nunesd66.pautas_api.pauta.model.Pauta;
import com.nunesd66.pautas_api.pauta.exception.PautaException;

import java.util.List;

public interface PautaService {
    PautaResponse save(PautaCreateRequest request) throws PautaException;
    List<PautaResponse> getAll();
    PautaResponse getLastRecord();
    PautaResponse fecharVotos(Long idPauta) throws PautaException;
}
