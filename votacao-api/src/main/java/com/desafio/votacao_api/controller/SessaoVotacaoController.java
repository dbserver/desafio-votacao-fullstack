package com.desafio.votacao_api.controller;

import com.desafio.votacao_api.dto.SessaoVotacaoRequestDTO;
import com.desafio.votacao_api.dto.SessaoVotacaoResponseDTO;
import com.desafio.votacao_api.dto.VotingResultDTO;
import jakarta.validation.Valid;
import com.desafio.votacao_api.service.SessaoVotacaoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * REST Controller for managing voting sessions (SessaoVotacao).
 * <p>
 * Orchestrates HTTP requests/responses. All business logic and DTO transformations
 * are delegated to the service layer.
 */
@RestController
@RequestMapping("/sessoes")
public class SessaoVotacaoController {

    private final SessaoVotacaoService sessaoVotacaoService;

    /**
     * Constructs a SessaoVotacaoController with the required service dependency.
     *
     * @param sessaoVotacaoService service for managing voting sessions
     */
    public SessaoVotacaoController(SessaoVotacaoService sessaoVotacaoService) {
        this.sessaoVotacaoService = sessaoVotacaoService;
    }

    /**
     * Opens a new voting session for a given agenda.
     *
     * @param request the session creation request
     * @return ResponseEntity with status 201 (Created) and the session data
     */
    @PostMapping
    public ResponseEntity<SessaoVotacaoResponseDTO> openSession(@Valid @RequestBody SessaoVotacaoRequestDTO request) {
        SessaoVotacaoResponseDTO response = sessaoVotacaoService.openSession(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Retrieves a voting session by its ID.
     *
     * @param id the unique identifier of the voting session
     * @return ResponseEntity with status 200 (OK) and the session data, or 404 if not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<SessaoVotacaoResponseDTO> getSessaoVotacaoById(@PathVariable UUID id) {
        SessaoVotacaoResponseDTO response = sessaoVotacaoService.getSessaoVotacaoById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieves the voting results for a specific voting session.
     * <p>
     * Returns the count of affirmative (SIM) and negative (NAO) votes, as well as
     * the voting outcome (APPROVED, REJECTED, or TIED).
     *
     * @param sessaoVotacaoId the unique identifier of the voting session
     * @return ResponseEntity with status 200 (OK) and the voting results
     */
    @GetMapping("/{sessaoVotacaoId}/results")
    public ResponseEntity<VotingResultDTO> getVotingResults(@PathVariable UUID sessaoVotacaoId) {
        VotingResultDTO result = sessaoVotacaoService.getVotingResults(sessaoVotacaoId);
        return ResponseEntity.ok(result);
    }
}





