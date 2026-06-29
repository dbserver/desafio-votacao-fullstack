package com.sousadev.voteapp.service;

import com.sousadev.voteapp.dtos.AssociateDto;
import com.sousadev.voteapp.entity.Associate;
import com.sousadev.voteapp.entity.Pauta;
import com.sousadev.voteapp.repository.AssociateRepository;
import com.sousadev.voteapp.repository.PautaRepository;
import com.sousadev.voteapp.utils.CpfValidatorUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AssociateService {

    private final AssociateRepository associateRepository;
    private final PautaRepository pautaRepository;

    public Associate create(AssociateDto request) {
        if (!CpfValidatorUtil.isValid(request.document())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "UNABLE_TO_VOTE");
        }

        Pauta pauta = pautaRepository.findById(request.pautaId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pauta não encontrada"));

        String document = CpfValidatorUtil.normalize(request.document());

        Optional<Associate> associateReady = associateRepository.findByDocument(document);

        Associate associate = Associate.builder()
                .name(request.name())
                .document(document)
                .pauta(pauta)
                .build();

        return associateRepository.save(associate);
    }
}
