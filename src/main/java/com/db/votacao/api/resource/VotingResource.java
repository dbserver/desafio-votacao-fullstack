package com.db.votacao.api.resource;

import com.db.votacao.api.dto.AgendaRequest;
import com.db.votacao.api.dto.VoteRequest;
import com.db.votacao.api.model.Agenda;
import com.db.votacao.api.model.Vote;
import com.db.votacao.api.model.VotingResult;
import com.db.votacao.api.model.VotingSession;
import com.db.votacao.api.service.VotingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Voting API", description = "API para gerenciamento de votações")
public class VotingResource {

    private final VotingService votingService;

    public VotingResource(VotingService votingService) {
        this.votingService = votingService;
    }

    @PostMapping("/agendas")
    @Operation(summary = "Criar nova pauta",
            description = "Cria uma nova pauta para votação")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pauta criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<Agenda> createAgenda(
            @Parameter(description = "Dados da pauta") @RequestBody AgendaRequest request) {
        return ResponseEntity.ok(votingService.createAgenda(request.getTitle(), request.getDescription()));
    }

    @PostMapping("/agendas/{agendaId}/sessions")
    @Operation(summary = "Abrir sessão de votação",
            description = "Abre uma nova sessão de votação para uma pauta específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sessão aberta com sucesso"),
            @ApiResponse(responseCode = "404", description = "Pauta não encontrada"),
            @ApiResponse(responseCode = "400", description = "Pauta já possui sessão aberta")
    })
    public ResponseEntity<VotingSession> openSession(
            @Parameter(description = "ID da pauta") @PathVariable Long agendaId,
            @Parameter(description = "Duração da sessão em minutos (opcional)")
            @RequestParam(required = false) Integer durationMinutes) {
        return ResponseEntity.ok(votingService.openVotingSession(agendaId, durationMinutes));
    }

    @PostMapping("/sessions/{sessionId}/votes")
    @Operation(summary = "Registrar voto",
            description = "Registra o voto de um associado em uma sessão de votação")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Voto registrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Voto inválido ou sessão fechada"),
            @ApiResponse(responseCode = "404", description = "Sessão não encontrada")
    })
    public ResponseEntity<Vote> vote(
            @Parameter(description = "ID da sessão de votação") @PathVariable Long sessionId,
            @Parameter(description = "Dados do voto") @RequestBody VoteRequest request) {
        return ResponseEntity.ok(votingService.registerVote(sessionId, request.getAssociateId(), request.isVote()));
    }

    @GetMapping("/sessions/{sessionId}/result")
    @Operation(summary = "Obter resultado da votação",
            description = "Retorna o resultado da votação de uma sessão específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resultado obtido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Sessão não encontrada")
    })
    public ResponseEntity<VotingResult> getResult(
            @Parameter(description = "ID da sessão de votação") @PathVariable Long sessionId) {
        return ResponseEntity.ok(votingService.getVotingResult(sessionId));
    }

    @GetMapping("/agendas")
    @Operation(summary = "Listar todas as pautas",
            description = "Retorna a lista de todas as pautas cadastradas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de pautas recuperada com sucesso")
    })
    public ResponseEntity<List<Agenda>> getAllAgendas() {
        return ResponseEntity.ok(votingService.getAllAgendas());
    }

    @GetMapping("/sessions")
    @Operation(summary = "Listar todas as sessões",
            description = "Retorna a lista de todas as sessões de votação")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de sessões recuperada com sucesso")
    })
    public ResponseEntity<List<VotingSession>> getAllSessions() {
        return ResponseEntity.ok(votingService.getAllSessions());
    }
}
