package com.desafio.votacao_api.controller;

import com.desafio.votacao_api.dto.PautaRequestDTO;
import com.desafio.votacao_api.dto.PautaResponseDTO;
import jakarta.validation.Valid;
import com.desafio.votacao_api.service.PautaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST Controller for managing voting agendas (Pauta).
 * <p>
 * Orchestrates HTTP requests/responses. All business logic and DTO transformations
 * are delegated to the service layer.
 */
@RestController
@RequestMapping("/pautas")
public class PautaController {

    private final PautaService pautaService;

    /**
     * Constructs a PautaController with the required service dependency.
     *
     * @param pautaService service for managing agendas
     */
    public PautaController(PautaService pautaService) {
        this.pautaService = pautaService;
    }

    /**
     * Creates a new voting agenda.
     *
     * @param request the agenda creation request containing name and description
     * @return ResponseEntity with status 201 (Created) and the created agenda data
     */
    @PostMapping
    public ResponseEntity<PautaResponseDTO> createPauta(@Valid @RequestBody PautaRequestDTO request) {
        PautaResponseDTO response = pautaService.createPauta(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Retrieves an agenda by its ID.
     *
     * @param id the unique identifier of the agenda
     * @return ResponseEntity with status 200 (OK) and the agenda data, or 404 if not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<PautaResponseDTO> getPautaById(@PathVariable UUID id) {
        PautaResponseDTO response = pautaService.getPautaById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieves all active agendas.
     *
     * @return ResponseEntity with status 200 (OK) and a list of active agendas
     */
    @GetMapping
    public ResponseEntity<List<PautaResponseDTO>> getAllActivePautas() {
        List<PautaResponseDTO> response = pautaService.findAllActiveIsTrue();
        return ResponseEntity.ok(response);
    }

    /**
     * Deactivates an agenda by its ID.
     *
     * @param id the unique identifier of the agenda to deactivate
     * @return ResponseEntity with status 204 (No Content)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePautaById(@PathVariable UUID id) {
        pautaService.deletePautaById(id);
        return ResponseEntity.noContent().build();
    }
}



