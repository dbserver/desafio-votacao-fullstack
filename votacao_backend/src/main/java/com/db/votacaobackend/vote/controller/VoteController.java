package com.db.votacaobackend.vote.controller;

import com.db.votacaobackend.vote.dto.VoteCreateDTO;
import com.db.votacaobackend.vote.dto.VoteDetailDTO;
import com.db.votacaobackend.vote.dto.VoteResultsDTO;
import com.db.votacaobackend.vote.service.VoteService;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/vote")
@AllArgsConstructor
public class VoteController {

  private VoteService service;

  @PostMapping
  @Operation(
      summary = "Criação de um voto",
      description = "Você poderá votar em uma sessão que esteja aberta."
          + "O cpf só pode votar uma vez na mesma sessão"
  )
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Voto realizado com sucesso"),
      @ApiResponse(
          responseCode = "400",
          description = "Sessão já encerrada",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class),
              examples = @ExampleObject(
                  value = "{ \"message\": \"Sessão já está encerrada!\" }"
              )
          )
      ),
      @ApiResponse(
          responseCode = "400",
          description = "Sessão já encerrada",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class),
              examples = @ExampleObject(
                  value = "{ \"message\": \"Sessão já está encerrada!\" }"
              )
          )
      ),
      @ApiResponse(
          responseCode = "409",
          description = "Cpf já votou nessa sessão",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class),
              examples = @ExampleObject(
                  value = "{ \"message\": \"Este CPF já votou!\" }"
              )
          )
      )
  })
  public ResponseEntity<VoteDetailDTO> createVote(@RequestBody VoteCreateDTO vote) {
    VoteDetailDTO createdVote = service.createVote(vote);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdVote);
  }

  @GetMapping
  @Operation(
      summary = "Listagem de todos os votos"
  )
  public ResponseEntity<List<VoteDetailDTO>> listAllVotes() {
    List<VoteDetailDTO> openSections = service.listAllVotes();
    return ResponseEntity.status(HttpStatus.OK).body(openSections);
  }


  @GetMapping("/results/{sectionId}")
  @Operation(
      summary = "Contabiliza os votos por sessão",
      description = "Contabiliza os votos através do id da sessão"
  )
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Contabilização dos votos"),
      @ApiResponse(
          responseCode = "404",
          description = "Sessão não encontrada para contabilizar seus votos",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class),
              examples = @ExampleObject(
                  value = "{ \"message\": \"Sessão não encontrada!\" }"
              )
          )
      )
  })
  public ResponseEntity<VoteResultsDTO> getVoteResults(@PathVariable("sectionId") Long sectionId) {
    VoteResultsDTO resultsDTO = service.listVotesFromSession(sectionId);
    return ResponseEntity.status(HttpStatus.OK).body(resultsDTO);
  }

}
