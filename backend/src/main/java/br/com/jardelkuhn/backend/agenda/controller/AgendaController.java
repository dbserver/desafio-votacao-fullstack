package br.com.jardelkuhn.backend.agenda.controller;

import br.com.jardelkuhn.backend.agenda.dto.*;
import br.com.jardelkuhn.backend.agenda.entity.Agenda;
import br.com.jardelkuhn.backend.agenda.mapper.AgendaConsolidationMapper;
import br.com.jardelkuhn.backend.agenda.service.AgendaService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/agendas")
@CrossOrigin(origins = "*")
public class AgendaController implements IAgendaResource {

    private final AgendaService agendaService;
    private final AgendaConsolidationMapper agendaConsolidationMapper;

    public AgendaController(AgendaService agendaService, AgendaConsolidationMapper agendaConsolidationMapper) {
        this.agendaService = agendaService;
        this.agendaConsolidationMapper = agendaConsolidationMapper;
    }

    @Override
    public ResponseEntity<AgendaConsolidationDTO> create(CreateAgendaDTO createAgendaDTO) {
        Agenda agenda = agendaService.createAgenda(createAgendaDTO);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(agenda.getId())
                .toUri();

        return ResponseEntity.created(location).body(agendaConsolidationMapper.fromAgenda(agenda));
    }

    @Override
    public ResponseEntity<SessionDTO> openSession(Long agendaId, OpenSessionDTO openSessionDTO) {
        LocalDateTime limitDate = agendaService.openAgendaAndReturnLimitDate(agendaId, openSessionDTO.duration());

        return ResponseEntity.status(HttpStatus.OK).body(new SessionDTO(limitDate));
    }

    @Override
    public ResponseEntity<List<AgendaConsolidationDTO>> findAll() {
        List<AgendaConsolidationDTO> agendaDTOS = agendaService.findAllAgendaVotingProjection().stream().map(agendaConsolidationMapper::fromProjection).toList();

        return ResponseEntity.ok(agendaDTOS);
    }

    @Override
    public void vote(Long agendaId, VoteDTO voteDTO) {
        agendaService.voteOnAgenda(agendaId, voteDTO);
    }
}