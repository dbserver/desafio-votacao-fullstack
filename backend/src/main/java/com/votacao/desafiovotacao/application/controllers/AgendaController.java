package com.votacao.desafiovotacao.application.controllers;

import com.votacao.desafiovotacao.application.dtos.AgendaDTO;
import com.votacao.desafiovotacao.domain.exceptions.AgendaNotValidException;
import com.votacao.desafiovotacao.domain.services.AgendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/agenda")
public class AgendaController {

    @Autowired
    private AgendaService agendaService;

    @PostMapping
    @CrossOrigin("*")
    public ResponseEntity<?> createAgenda(@RequestBody AgendaDTO agendaDTO) throws AgendaNotValidException {
            return new ResponseEntity<>(agendaService.createAgenda(agendaDTO), HttpStatus.CREATED);
    }

    @GetMapping
    @CrossOrigin("*")
    public ResponseEntity<?> findAll() {
        return new ResponseEntity<>(agendaService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/id")
    @CrossOrigin("*")
    public ResponseEntity<?> get(@RequestParam(value = "id") String id) {
        return new ResponseEntity<>(agendaService.get(id), HttpStatus.OK);
    }

    @PutMapping("/id")
    @CrossOrigin("*")
    public ResponseEntity<?> update(@RequestParam(value = "id") String id, @RequestBody AgendaDTO agendaDTO) {
        return new ResponseEntity<>(agendaService.update(id, agendaDTO), HttpStatus.OK);
    }

    @DeleteMapping("/id")
    @CrossOrigin("*")
    public ResponseEntity<?> delete(@RequestParam(value = "id") String id) {
        agendaService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping
    @CrossOrigin("*")
    public ResponseEntity<?> deleteAll() {
        agendaService.deleteAll();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
