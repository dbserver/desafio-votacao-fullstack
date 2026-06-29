package com.sousadev.voteapp.service;

import com.sousadev.voteapp.dtos.PautaDto;
import com.sousadev.voteapp.dtos.PautaResponseDto;
import com.sousadev.voteapp.dtos.VoteCountDto;
import com.sousadev.voteapp.dtos.PautaResponseDto.VoteResponseDto;
import com.sousadev.voteapp.entity.Pauta;
import com.sousadev.voteapp.enums.VoteValue;
import com.sousadev.voteapp.repository.PautaRepository;
import com.sousadev.voteapp.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PautaService {

    private final PautaRepository repository;
    private final VoteRepository voteRepository;

    public ResponseEntity<PautaResponseDto> create(PautaDto request) {
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime expireIn = request.expireIn();

        Pauta pauta = Pauta.builder()
                .name(request.name())
                .description(request.description())
                .createdAt(createdAt)
                .expireIn(expireIn)
                .build();

        var saved = repository.save(pauta);
        var dto = PautaResponseDto.fromEntity(saved);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    public List<PautaResponseDto> findAll() {
        var pautas = repository.findAll();
        log.info("findAll() called, number of pautas retrieved: {}", pautas.size());
        return pautas.stream()
                .map(PautaResponseDto::fromEntity)
                .toList();
    }

    public PautaResponseDto findById(UUID id) {
        Pauta pauta = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pauta não encontrada"));
        return PautaResponseDto.fromEntity(pauta);
    }

    public VoteCountDto countVotes(UUID pautaId) {
        repository.findById(pautaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pauta não encontrada"));

        long simCount = voteRepository.countByPautaIdAndValue(pautaId, VoteValue.SIM);
        long naoCount = voteRepository.countByPautaIdAndValue(pautaId, VoteValue.NAO);

        return VoteCountDto.builder()
                .votes(VoteCountDto.Votes.builder()
                        .sim((int) simCount)
                        .nao((int) naoCount)
                        .build())
                .build();
    }

    public List<VoteResponseDto> getVotes(UUID pautaId) {
        return voteRepository.findByPautaId(pautaId).stream()
                .map(VoteResponseDto::fromEntity)
                .toList();
    }
}