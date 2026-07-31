package br.com.jardelkuhn.backend.controller;

import br.com.jardelkuhn.backend.dto.CreateAgendaDTO;
import br.com.jardelkuhn.backend.dto.OpenSessionDTO;
import br.com.jardelkuhn.backend.dto.SessionDTO;
import br.com.jardelkuhn.backend.entity.Agenda;
import br.com.jardelkuhn.backend.service.AgendaService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/agendas")
@CrossOrigin(origins = "*")
public class AgendaController implements IAgendaResource {

    private final AgendaService agendaService;

    public AgendaController(AgendaService agendaService) {
        this.agendaService = agendaService;
    }

    @PostMapping
    public ResponseEntity<Agenda> create(@RequestBody CreateAgendaDTO createAgendaDTO) {
        Agenda agenda = agendaService.createAgenda(createAgendaDTO);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(agenda.getId())
                .toUri();

        return ResponseEntity.created(location).body(agenda);
    }

    @PostMapping("/{agendaId}/open")
    public ResponseEntity<SessionDTO> openSession(
            @PathVariable Long agendaId,
            @Valid @RequestBody OpenSessionDTO openSessionDTO) {
        SessionDTO sessionDTO = agendaService.openSession(agendaId, openSessionDTO);

        return ResponseEntity.status(HttpStatus.OK).body(sessionDTO);
    }

    @GetMapping
    public ResponseEntity<List<Agenda>> getAll() {
        return ResponseEntity.ok(agendaService.listAllAgendas());
    }
}