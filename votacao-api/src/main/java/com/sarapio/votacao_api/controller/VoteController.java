package com.sarapio.votacao_api.controller;

import com.sarapio.votacao_api.domain.session.Session;
import com.sarapio.votacao_api.domain.vote.VoteRequestDTO;
import com.sarapio.votacao_api.service.VoteService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/vote")
public class VoteController {

    private final VoteService voteService;

    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    @PostMapping(value = "/create/{associateId}/{sessionId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createVote(@PathVariable UUID associateId, @PathVariable UUID sessionId, @RequestBody VoteRequestDTO data) {
        String result = voteService.createVote(associateId, sessionId, data);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/result")
    public ResponseEntity<List<Session>> results() {
        List<Session> result = voteService.results();
        return ResponseEntity.ok(result);
    }

}
