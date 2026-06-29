package com.sousadev.voteapp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.sousadev.voteapp.dtos.VoteDto;
import com.sousadev.voteapp.entity.Vote;
import com.sousadev.voteapp.service.VoteService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RestController
@RequestMapping("/api/vote")
@RequiredArgsConstructor
public class VoteController {
    private final VoteService voteService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Vote create(
            @RequestBody @Valid VoteDto request) {
        return voteService.create(request);
    }
}
