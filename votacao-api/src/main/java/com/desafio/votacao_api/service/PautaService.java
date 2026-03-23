package com.desafio.votacao_api.service;

import com.desafio.votacao_api.dto.PautaRequestDTO;
import com.desafio.votacao_api.dto.PautaResponseDTO;
import com.desafio.votacao_api.exception.ErrorOnCreatePautaException;
import com.desafio.votacao_api.exception.PautaNotFoundException;
import com.desafio.votacao_api.model.Pauta;
import com.desafio.votacao_api.repository.PautaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Service for managing voting agendas (Pauta).
 *
 * Provides business logic for creating, retrieving, and managing agendas.
 * All DTO transformations are handled here, not in controllers.
 */
@Service
public class PautaService {

    private final PautaRepository pautaRepository;

    /**
     * Constructs a PautaService with the required repository dependency.
     *
     * @param pautaRepository the repository for persisting agendas
     */
    public PautaService(PautaRepository pautaRepository) {
        this.pautaRepository = pautaRepository;
    }

    /**
     * Creates a new voting agenda and returns response DTO.
     *
     * @param request the request data containing agenda name and description
     * @return PautaResponseDTO with the created agenda data
     * @throws ErrorOnCreatePautaException if an error occurs during creation
     */
    public PautaResponseDTO createPauta(PautaRequestDTO request) {
        Pauta pauta = new Pauta(request.name(), request.description());
        Pauta savedPauta = pautaRepository.save(pauta);
        return PautaResponseDTO.fromEntity(savedPauta);
    }

    /**
     * Retrieves an agenda by its unique identifier (internal use for validation).
     * Returns the entity, not DTO - for internal service operations.
     *
     * @param id the unique identifier of the agenda
     * @return the requested Pauta entity
     * @throws PautaNotFoundException if the agenda is not found
     */
    protected Pauta getPautaByIdInternal(UUID id) {
        return this.pautaRepository.findById(id).orElseThrow(() ->
                new PautaNotFoundException("Pauta with id " + id + " not found."));
    }

    /**
     * Retrieves an agenda by its unique identifier and returns response DTO.
     *
     * @param id the unique identifier of the agenda
     * @return PautaResponseDTO with the requested agenda data
     * @throws PautaNotFoundException if the agenda is not found
     */
    public PautaResponseDTO getPautaById(UUID id) {
        Pauta pauta = this.pautaRepository.findById(id).orElseThrow(() ->
                new PautaNotFoundException("Pauta with id " + id + " not found."));
        return PautaResponseDTO.fromEntity(pauta);
    }

    /**
     * Retrieves all active agendas and returns list of response DTOs.
     *
     * @return a list of PautaResponseDTOs for all active agendas
     */
    public List<PautaResponseDTO> findAllActiveIsTrue() {
        List<Pauta> pautas = pautaRepository.findAllByIsActiveTrue();
        return pautas.stream()
                .map(PautaResponseDTO::fromEntity)
                .toList();
    }

    /**
     * Deactivates an agenda by setting its active status to false.
     *
     * @param id the unique identifier of the agenda to deactivate
     * @throws PautaNotFoundException if the agenda is not found
     */
    public void deletePautaById(UUID id) {
        Pauta pauta = this.pautaRepository.findById(id).orElseThrow(() ->
                new PautaNotFoundException("Pauta with id " + id + " not found."));
        pauta.setActive(false);
        this.pautaRepository.save(pauta);
    }

}
