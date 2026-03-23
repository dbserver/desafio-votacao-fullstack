package com.desafio.votacao_api.service;

import com.desafio.votacao_api.dto.VotoRequestDTO;
import com.desafio.votacao_api.dto.VotoResponseDTO;
import com.desafio.votacao_api.exception.CpfAlreadyVotedOnPautaException;
import com.desafio.votacao_api.exception.SessaoVotacaoNotFoundException;
import com.desafio.votacao_api.exception.SessionClosedException;
import com.desafio.votacao_api.exception.VotoNotFoundException;
import com.desafio.votacao_api.model.Cpf;
import com.desafio.votacao_api.model.SessaoVotacao;
import com.desafio.votacao_api.model.Voto;
import com.desafio.votacao_api.repository.VotoRepository;
import com.desafio.votacao_api.service.facade.CpfValidationFacade;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Service for managing votes (Voto).
 *
 * Provides business logic for creating votes in voting sessions,
 * including validation that sessions are still open, and handling DTO transformations.
 */
@Service
public class VotoService {

    private final VotoRepository votoRepository;
    private final SessaoVotacaoService sessaoVotacaoService;
    private final CpfValidationFacade cpfValidationFacade;

    /**
     * Constructs a VotoService with the required dependencies.
     *
     * @param votoRepository repository for persisting votes
     * @param sessaoVotacaoService service for managing voting sessions
     * @param cpfValidationFacade facade for validating CPF eligibility
     */
    public VotoService(
            VotoRepository votoRepository,
            SessaoVotacaoService sessaoVotacaoService,
            CpfValidationFacade cpfValidationFacade) {
        this.votoRepository = votoRepository;
        this.sessaoVotacaoService = sessaoVotacaoService;
        this.cpfValidationFacade = cpfValidationFacade;
    }

    /**
     * Registers a vote in a voting session and returns response DTO.
     *
     * @param request the request containing session ID, vote value, and CPF
     * @return VotoResponseDTO with the registered vote data (no CPF exposed)
     * @throws SessaoVotacaoNotFoundException if the voting session is not found
     * @throws SessionClosedException if the voting session is closed
     */
    public VotoResponseDTO registerVote(VotoRequestDTO request) {
        SessaoVotacao sessao = this.sessaoVotacaoService.getSessaoVotacaoByIdInternal(request.sessaoVotacaoId());
        UUID pautaId = sessao.getPauta().getId();

        if (!sessao.isOpen()) {
            throw new SessionClosedException("Voting session is closed. No more votes can be registered.");
        }

        if (votoRepository.existsActiveVoteByPautaIdAndCpf(pautaId, request.cpf())) {
            throw new CpfAlreadyVotedOnPautaException(
                    "CPF " + request.cpf() + " has already voted on agenda " + pautaId + "."
            );
        }

        cpfValidationFacade.validateCpfNumber(request.cpf());

        Cpf cpf = new Cpf(request.cpf());
        Voto voto = new Voto(sessao, request.vote(), cpf);
        Voto savedVoto = votoRepository.save(voto);
        
        return VotoResponseDTO.fromEntity(savedVoto);
    }

    /**
     * Retrieves a vote by its ID and returns response DTO.
     *
     * @param id the unique identifier of the vote
     * @return VotoResponseDTO with the vote data (no CPF exposed)
     * @throws VotoNotFoundException if the vote is not found
     */
    public VotoResponseDTO getVotoById(UUID id) {
        Voto voto = votoRepository.findById(id).orElseThrow(() ->
                new VotoNotFoundException("Vote with id " + id + " not found."));
        return VotoResponseDTO.fromEntity(voto);
    }

    /**
     * Retrieves a vote by its ID (internal use).
     * Returns the entity, not DTO - for internal service operations.
     *
     * @param id the unique identifier of the vote
     * @return the Voto entity
     * @throws VotoNotFoundException if the vote is not found
     */
    protected Voto getVotoByIdInternal(UUID id) {
        return votoRepository.findById(id).orElseThrow(() ->
                new VotoNotFoundException("Vote with id " + id + " not found."));
    }
}






