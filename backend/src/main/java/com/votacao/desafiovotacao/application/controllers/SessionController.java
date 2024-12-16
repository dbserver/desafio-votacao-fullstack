package com.votacao.desafiovotacao.application.controllers;

import com.votacao.desafiovotacao.application.dtos.SessionDTO;
import com.votacao.desafiovotacao.application.dtos.VoteResponseDTO;
import com.votacao.desafiovotacao.domain.exceptions.AgendaNotFoundException;
import com.votacao.desafiovotacao.domain.exceptions.SessionTimeException;
import com.votacao.desafiovotacao.domain.services.SessionService;
import com.votacao.desafiovotacao.domain.services.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/session")
public class SessionController {

    @Autowired
    private SessionService sessionService;
    @Autowired
    private VoteService voteService;

    @PostMapping("/create")
    @CrossOrigin("*")
    public ResponseEntity<?> createSession(@RequestBody SessionDTO sessionDTO)
            throws AgendaNotFoundException, SessionTimeException {
            return new ResponseEntity<>(sessionService.createSession(sessionDTO), HttpStatus.CREATED);
    }

    @GetMapping("/id")
    @CrossOrigin("*")
    public ResponseEntity<?> get(@RequestParam(value = "id") String id) {
        return new ResponseEntity<>(sessionService.get(id), HttpStatus.OK);
    }

    @GetMapping
    @CrossOrigin("*")
    public ResponseEntity<?> findAll() {
        return new ResponseEntity<>(sessionService.findAll(), HttpStatus.OK);
    }

    @PutMapping("/id")
    @CrossOrigin("*")
    public ResponseEntity<?> update(@RequestParam(value = "id") String id, @RequestBody SessionDTO sessionDTO) {
        return new ResponseEntity<>(sessionService.update(id, sessionDTO), HttpStatus.OK);
    }

    @DeleteMapping("/id")
    @CrossOrigin("*")
    public ResponseEntity<?> delete(@RequestParam(value = "id") String id) {
        sessionService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping
    @CrossOrigin("*")
    public ResponseEntity<?> deleteAll() {
        sessionService.deleteAll();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/result")
    public ResponseEntity<VoteResponseDTO> getVotesResult(@RequestParam(value = "sessionId") String sessionId) {
        return voteService.getVotesResult(sessionId);
    }
}
