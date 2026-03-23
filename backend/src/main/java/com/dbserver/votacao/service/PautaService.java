package com.dbserver.votacao.service;

import com.dbserver.votacao.dto.PautaRequest;
import com.dbserver.votacao.dto.PautaResponse;
import com.dbserver.votacao.entity.Pauta;
import com.dbserver.votacao.exception.NotFoundException;
import com.dbserver.votacao.repository.PautaRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PautaService {

    private static final Logger log = LoggerFactory.getLogger(PautaService.class);
    private final PautaRepository pautaRepository;

    public PautaResponse criar(PautaRequest request) {
        log.info("Criando nova pauta: {}", request.titulo());

        Pauta pauta = new Pauta();
        pauta.setTitulo(request.titulo());
        pauta.setDescricao(request.descricao());

        Pauta salva = pautaRepository.save(pauta);
        log.info("Pauta criada com id: {}", salva.getId());

        return toResponse(salva);
    }

    public List<PautaResponse> listarTodas() {
        log.info("Listando todas as pautas");
        return pautaRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public PautaResponse buscarPorId(Long id) {
        log.info("Buscando pauta por id: {}", id);
        return toResponse(findById(id));
    }

    public Pauta findById(Long id) {
        return pautaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Pauta não encontrada com id: " + id));
    }

    private PautaResponse toResponse(Pauta pauta) {
        return new PautaResponse(
                pauta.getId(),
                pauta.getTitulo(),
                pauta.getDescricao(),
                pauta.getCriadaEm()
        );
    }
}
