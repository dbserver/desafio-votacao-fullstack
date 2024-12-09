package com.sarapio.votacao_api.controller;

import com.sarapio.votacao_api.domain.session.Session;
import com.sarapio.votacao_api.domain.session.SessionRequestDTO;
import com.sarapio.votacao_api.service.SessionService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/session")
public class SessionController {

    private final SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @PostMapping(value = "/createSession/{topicId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Session> createSession(@PathVariable UUID topicId, @RequestBody SessionRequestDTO data) {
        Session sessions = sessionService.createSession(topicId, data);
        return ResponseEntity.ok(sessions);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Session> listSessions() {
        return sessionService.listSession();
    }

}
