package com.nunesd66.pautas_api.pauta.service;

import com.nunesd66.pautas_api.pauta.dto.PautaCreateRequest;
import com.nunesd66.pautas_api.pauta.dto.PautaResponse;
import com.nunesd66.pautas_api.pauta.mapper.PautaMapper;
import com.nunesd66.pautas_api.pauta.model.Pauta;
import com.nunesd66.pautas_api.pauta.exception.PautaException;
import com.nunesd66.pautas_api.pauta.repository.PautaRepository;
import com.nunesd66.pautas_api.pauta.validator.PautaValidator;
import com.nunesd66.pautas_api.singleton.ContagemVotosSingleton;
import com.nunesd66.pautas_api.voto.model.Voto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PautaServiceImpl implements PautaService {

    private final PautaRepository repository;
    private final PautaValidator validator;
    private final PautaMapper mapper;

    @Override
    public PautaResponse save(PautaCreateRequest request) throws PautaException {

        Pauta pauta = mapper.toEntity(request);
        this.validator.validCreatePauta(pauta);

        pauta.getSessaoVotacao().setPauta(pauta);
        Pauta pautaSave = this.repository.save(pauta);
        return mapper.toResponse(pautaSave);
    }

    @Override
    public List<PautaResponse> getAll() {
        return this.repository.findAllClosed()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public PautaResponse getLastRecord() {
        return this.repository.getLastRecord()
                .map(mapper::toResponse)
                .orElse(null);
    }

    @Override
    public PautaResponse fecharVotos(Long idPauta) throws PautaException {
        ContagemVotosSingleton singleton = ContagemVotosSingleton.getInstancia();

        Pauta pauta = this.repository.findById(idPauta).orElseThrow(() ->
                new PautaException("Não foi possível fechar os votos, código da pauta não encontrada."));

        List<Voto> votos = singleton.getVotos()
                .stream()
                .map(voto -> {
                    Voto newVoto = new Voto();
                    newVoto.setVoto(voto.getVoto());
                    newVoto.setCpfAssociado(voto.getCpfAssociado());
                    newVoto.setSessaoVotacao(pauta.getSessaoVotacao());
                    return newVoto;
                }).toList();

        pauta.getSessaoVotacao().getVotos().addAll(votos);
        Pauta pautaSave = this.repository.save(pauta);

        singleton.resetarVotos();
        return mapper.toResponse(pautaSave);
    }

}
