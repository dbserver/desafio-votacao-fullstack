package com.db.votacaobackend.agenda.controller;

import com.db.votacaobackend.agenda.dto.AgendaDTO;
import com.db.votacaobackend.agenda.mapper.AgendaMapper;
import com.db.votacaobackend.agenda.model.Agenda;
import com.db.votacaobackend.agenda.service.AgendaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/agenda")
@AllArgsConstructor
public class AgendaController {

  private final AgendaService service;
  private final AgendaMapper mapper;

  @PostMapping
  @Operation(
      summary = "Criação de uma nova pauta",
      requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = AgendaDTO.class),
              examples = @ExampleObject(value = "{ \"name\": \"Horário da daily às 10 horas\" }")
          )
      )
  )
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Pauta criada com sucesso"),
      @ApiResponse(
          responseCode = "400",
          description = "Criação de uma pauta sem nome especificado",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class),
              examples = @ExampleObject(
                  value = "{ \"message\": \"O nome da pauta é obrigatório\" }"
              )
          )
      )
  })
  public ResponseEntity<AgendaDTO> createAgenda(@RequestBody AgendaDTO agendaDto) {
    Agenda agenda = mapper.toEntity(agendaDto);
    Agenda savedAgenda = service.createAgenda(agenda);
    return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDto(savedAgenda));
  }

  @GetMapping
  @Operation(
      summary = "Lista todas as pautas"
  )
  public ResponseEntity<List<AgendaDTO>> listAllAgendas() {
    List<Agenda> allAgendas = service.listAllAgendas();
    List<AgendaDTO> allAgendasDto = allAgendas.stream()
        .map(mapper::toDto)
        .toList();
    return ResponseEntity.status(HttpStatus.OK).body(allAgendasDto);
  }
}