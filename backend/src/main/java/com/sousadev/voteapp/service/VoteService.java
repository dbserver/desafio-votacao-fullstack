package com.sousadev.voteapp.service;

import com.sousadev.voteapp.dtos.VoteDto;
import com.sousadev.voteapp.entity.Associate;
import com.sousadev.voteapp.entity.Pauta;
import com.sousadev.voteapp.entity.Vote;
import com.sousadev.voteapp.repository.AssociateRepository;
import com.sousadev.voteapp.repository.PautaRepository;
import com.sousadev.voteapp.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VoteService {

    private final VoteRepository voteRepository;
    private final AssociateRepository associateRepository;
    private final PautaRepository pautaRepository;

    public Vote create(VoteDto request) {
        Pauta pauta = pautaRepository.findById(request.pautaId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pauta não encontrada"));

        Associate associate = associateRepository.findByDocument(request.document())
                .orElseGet(() -> {
                    Associate associateTransient = new Associate();
                    associateTransient.setDocument(request.document());
                    associateTransient.setPauta(pauta);
                    associateTransient.setName("Associado " + request.document());
                    return associateRepository.save(associateTransient);
                });

        UUID associateId = associate.getId();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expirationTime = pauta.getExpireIn();

        if (now.isAfter(expirationTime)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Votação encerrada para esta pauta");
        }

        if (voteRepository.findByAssociateIdAndPautaId(associateId, request.pautaId()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Associado já votou nesta pauta");
        }

        Vote vote = Vote.builder()
                .associate(associate)
                .pauta(pauta)
                .value(request.value())
                .build();

        return voteRepository.save(vote);
    }
}
