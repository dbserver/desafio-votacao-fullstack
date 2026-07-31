package br.com.jardelkuhn.backend.controller;

import br.com.jardelkuhn.backend.dto.CreateAgendaDTO;
import br.com.jardelkuhn.backend.dto.OpenSessionDTO;
import br.com.jardelkuhn.backend.dto.SessionDTO;
import br.com.jardelkuhn.backend.entity.Agenda;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Tag(name = "Gerenciamento de Pautas", description = "Endpoints para criação, controle de sessão e votação de pautas")
public interface IAgendaResource {

    @Operation(summary = "Cadastra uma nova pauta", description = "Cria uma pauta sem sessão de votação aberta por padrão")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pauta criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados da pauta inválidos")
    })
    @PostMapping
    ResponseEntity<Agenda> create(@RequestBody @Valid CreateAgendaDTO agendaDTO);

    @Operation(summary = "Abre uma sessão de votação", description = "Define um limite de tempo (duration: long) para a votação de uma pauta específica")
    @PostMapping("/{agendaId}/open")
    ResponseEntity<SessionDTO> openSession(@PathVariable Long agendaId, @Valid @RequestBody OpenSessionDTO openSessionDTO);

    @Operation(summary = "Lista todas as pautas", description = "Lista todas as pautas cadastradas")
    @GetMapping
    ResponseEntity<List<Agenda>> getAll();
}
