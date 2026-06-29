package com.sousadev.voteapp.controller;

import com.sousadev.voteapp.dtos.PautaDto;
import com.sousadev.voteapp.dtos.PautaResponseDto;
import com.sousadev.voteapp.dtos.VoteCountDto;
import com.sousadev.voteapp.dtos.PautaResponseDto.VoteResponseDto;
import com.sousadev.voteapp.dtos.VoteDto;
import com.sousadev.voteapp.service.PautaService;
import com.sousadev.voteapp.service.VoteService;
import com.sousadev.voteapp.entity.Vote;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/pautas")
@RequiredArgsConstructor
public class PautaController {

    private final PautaService pautaService;
    private final VoteService voteService;

    @PostMapping
    public ResponseEntity<PautaResponseDto> create(@RequestBody PautaDto request) {
        return pautaService.create(request);
    }

    @GetMapping
    public List<PautaResponseDto> findAll() {
        return pautaService.findAll();
    }

    @GetMapping("/{id}")
    public PautaResponseDto findById(
            @PathVariable UUID id) {
        return pautaService.findById(id);
    }

    @GetMapping("/{id}/votes/count")
    public VoteCountDto countVotes(
            @PathVariable UUID id) {
        return pautaService.countVotes(id);
    }

    @GetMapping("/{id}/votes")
    public List<VoteResponseDto> getVotes(@PathVariable UUID id, @RequestParam(required = false) String voteValue) {

        return pautaService.getVotes(id);
    }

    @PostMapping("/{id}/vote")
    public VoteResponseDto vote(@PathVariable UUID id, @RequestBody VoteDto request) {
        Vote vote = voteService.create(request);
        return VoteResponseDto.fromEntity(vote);
    }

}