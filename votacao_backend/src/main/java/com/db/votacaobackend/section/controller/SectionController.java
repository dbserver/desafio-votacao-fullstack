package com.db.votacaobackend.section.controller;

import com.db.votacaobackend.agenda.dto.AgendaDTO;
import com.db.votacaobackend.agenda.mapper.AgendaMapper;
import com.db.votacaobackend.agenda.model.Agenda;
import com.db.votacaobackend.section.dto.CreateSectionDTO;
import com.db.votacaobackend.section.dto.SectionDetailsDTO;
import com.db.votacaobackend.section.service.SectionService;
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
@RequestMapping("/v1/section")
@AllArgsConstructor
public class SectionController {

  private final SectionService service;
  private final AgendaMapper agendaMapper;

  @PostMapping
  @Operation(
      summary = "Criação de uma sessão",
      description = "Cria uma sessão para uma pauta existente. Somente é permitido criar sessões para pautas que ainda não possuem sessão vinculada."
  )
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Sessão salva com sucesso"),
      @ApiResponse(
          responseCode = "404",
          description = "Pauta não encontrada para realizar a criação da sessão",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class),
              examples = @ExampleObject(
                  value = "{ \"message\": \"Pauta não encontrada\" }"
              )
          )
      )
  })
  public ResponseEntity<SectionDetailsDTO> createSection(@RequestBody CreateSectionDTO section) {
    SectionDetailsDTO savedSection = service.createSection(section);
    return ResponseEntity.status(HttpStatus.CREATED).body(savedSection);
  }

  @GetMapping("/open")
  @Operation(
      summary = "Listagem das sessões que estão abertas",
      description = "Listas as sessões que ainda estão com o tempo final posterior ao momento atual"
  )
  public ResponseEntity<List<SectionDetailsDTO>> listOpenSections() {
    List<SectionDetailsDTO> openSections = service.listOpenSections();
    return ResponseEntity.status(HttpStatus.OK).body(openSections);
  }

  @GetMapping
  @Operation(
      summary = "Listagem de todas as sessões"
  )
  public ResponseEntity<List<SectionDetailsDTO>> listAllSessions() {
    List<SectionDetailsDTO> openSections = service.listAllSessions();
    return ResponseEntity.status(HttpStatus.OK).body(openSections);
  }

  @Operation(
      summary = "Listagem das pautas disponíveis",
      description = "Listas as pautas disponíveis para que possa ser criada uma sessão"
  )
  @GetMapping("/availiable-agendas")
  public ResponseEntity<List<AgendaDTO>> listAllAvailiableAgendas() {
    List<Agenda> availiableAgendas = service.listAllAvailiableAgendas();
    List<AgendaDTO> availiableAgendasDto = availiableAgendas.stream()
        .map(agendaMapper::toDto)
        .toList();
    return ResponseEntity.status(HttpStatus.OK).body(availiableAgendasDto);
  }
}
