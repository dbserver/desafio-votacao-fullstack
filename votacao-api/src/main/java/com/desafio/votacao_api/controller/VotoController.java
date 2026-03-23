package com.desafio.votacao_api.controller;

import com.desafio.votacao_api.dto.VotoRequestDTO;
import com.desafio.votacao_api.dto.VotoResponseDTO;
import jakarta.validation.Valid;
import com.desafio.votacao_api.service.VotoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * REST Controller for managing votes (Voto).
 * <p>
 * Orchestrates HTTP requests/responses. All business logic and DTO transformations
 * are delegated to the service layer.
 */
@RestController
@RequestMapping("/votos")
public class VotoController {

    private final VotoService votoService;

    /**
     * Constructs a VotoController with the required service dependency.
     *
     * @param votoService service for managing votes
     */
    public VotoController(VotoService votoService) {
        this.votoService = votoService;
    }

    /**
     * Registers a new vote in a voting session.
     *
     * @param request the vote registration request
     * @return ResponseEntity with status 201 (Created) and the vote data
     */
    @PostMapping
    public ResponseEntity<VotoResponseDTO> registerVote(@Valid @RequestBody VotoRequestDTO request) {
        VotoResponseDTO response = votoService.registerVote(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Retrieves a vote by its ID.
     *
     * @param id the unique identifier of the vote
     * @return ResponseEntity with status 200 (OK) and the vote data, or 404 if not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<VotoResponseDTO> getVotoById(@PathVariable UUID id) {
        VotoResponseDTO response = votoService.getVotoById(id);
        return ResponseEntity.ok(response);
    }
}



