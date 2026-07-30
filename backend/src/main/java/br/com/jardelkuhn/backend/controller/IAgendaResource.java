package br.com.jardelkuhn.backend.controller;

import br.com.jardelkuhn.backend.dto.CreateAgendaDTO;
import br.com.jardelkuhn.backend.entity.Agenda;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface IAgendaResource {
    @PostMapping
    ResponseEntity<Agenda> create(@RequestBody @Valid CreateAgendaDTO agendaDTO);

    @GetMapping
    ResponseEntity<List<Agenda>> getAll();
}
