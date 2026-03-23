package com.desafio.votacao_api.service;

import com.desafio.votacao_api.dto.SessaoVotacaoRequestDTO;
import com.desafio.votacao_api.dto.SessaoVotacaoResponseDTO;
import com.desafio.votacao_api.dto.VotingResultDTO;
import com.desafio.votacao_api.exception.PautaNotFoundException;
import com.desafio.votacao_api.exception.SessaoVotacaoNotFoundException;
import com.desafio.votacao_api.exception.SessionAlreadyExistsForPautaException;
import com.desafio.votacao_api.model.Pauta;
import com.desafio.votacao_api.model.SessaoVotacao;
import com.desafio.votacao_api.model.enums.VotoValue;
import com.desafio.votacao_api.repository.SessaoVotacaoRepository;
import com.desafio.votacao_api.repository.VotoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Service for managing voting sessions (SessaoVotacao).
 *
 * Provides business logic for opening voting sessions, managing their lifecycle,
 * calculating voting results, and handling DTO transformations.
 */
@Service
public class SessaoVotacaoService {

    private final SessaoVotacaoRepository sessaoVotacaoRepository;
    private final VotoRepository votoRepository;
    private final PautaService pautaService;

    /**
     * Constructs a SessaoVotacaoService with the required dependencies.
     *
     * @param sessaoVotacaoRepository repository for persisting voting sessions
     * @param votoRepository repository for retrieving votes
     * @param pautaService service for managing agendas
     */
    public SessaoVotacaoService(SessaoVotacaoRepository sessaoVotacaoRepository,
                                VotoRepository votoRepository,
                                PautaService pautaService) {
        this.sessaoVotacaoRepository = sessaoVotacaoRepository;
        this.votoRepository = votoRepository;
        this.pautaService = pautaService;
    }

    /**
     * Opens a new voting session for a given agenda and returns response DTO.
     *
     * @param request the request containing the agenda ID and optional duration
     * @return SessaoVotacaoResponseDTO with the created session data
     * @throws PautaNotFoundException if the agenda is not found
     */
    public SessaoVotacaoResponseDTO openSession(SessaoVotacaoRequestDTO request) {
        Pauta pauta = this.pautaService.getPautaByIdInternal(request.pautaId());

        if (sessaoVotacaoRepository.existsByPautaId(request.pautaId())) {
            throw new SessionAlreadyExistsForPautaException(
                    "Agenda with id " + request.pautaId() + " already has a voting session."
            );
        }

        int durationInSeconds = request.durationInSeconds() != null ? request.durationInSeconds() : 0;
        SessaoVotacao sessao = SessaoVotacao.openSession(pauta, durationInSeconds);
        SessaoVotacao savedSessao = sessaoVotacaoRepository.save(sessao);
        return SessaoVotacaoResponseDTO.fromEntity(savedSessao);
    }

    /**
     * Retrieves a voting session by its ID and returns response DTO.
     *
     * @param id the unique identifier of the voting session
     * @return SessaoVotacaoResponseDTO with the session data
     * @throws SessaoVotacaoNotFoundException if the session is not found
     */
    public SessaoVotacaoResponseDTO getSessaoVotacaoById(UUID id) {
        SessaoVotacao sessao = sessaoVotacaoRepository.findById(id).orElseThrow(() ->
                new SessaoVotacaoNotFoundException("Voting session with id " + id + " not found."));
        return SessaoVotacaoResponseDTO.fromEntity(sessao);
    }

    /**
     * Retrieves all voting sessions associated with a given agenda.
     *
     * @param pautaId the unique identifier of the agenda
     * @return list of sessions for the agenda
     * @throws PautaNotFoundException if the agenda does not exist
     */
    public List<SessaoVotacaoResponseDTO> getSessoesByPautaId(UUID pautaId) {
        this.pautaService.getPautaByIdInternal(pautaId);

        List<SessaoVotacao> sessoes = sessaoVotacaoRepository.findAllByPautaIdOrderByCreatedAtDesc(pautaId);
        return sessoes.stream()
                .map(SessaoVotacaoResponseDTO::fromEntity)
                .toList();
    }

    /**
     * Retrieves a voting session by its ID (internal use for validation).
     *
     * @param id the unique identifier of the voting session
     * @return the SessaoVotacao entity
     * @throws SessaoVotacaoNotFoundException if the session is not found
     */
    protected SessaoVotacao getSessaoVotacaoByIdInternal(UUID id) {
        return sessaoVotacaoRepository.findById(id).orElseThrow(() ->
                new SessaoVotacaoNotFoundException("Voting session with id " + id + " not found."));
    }

    /**
     * Calculates the voting results for a specific voting session.
     *
     * @param sessaoVotacaoId the ID of the voting session
     * @return a VotingResultDTO containing vote counts and the result
     * @throws SessaoVotacaoNotFoundException if the session is not found
     */
    public VotingResultDTO getVotingResults(UUID sessaoVotacaoId) {
        this.getSessaoVotacaoByIdInternal(sessaoVotacaoId);

        long votesYes = votoRepository.countBySessaoVotacaoIdAndVoteValue(sessaoVotacaoId, VotoValue.SIM);
        long votesNo = votoRepository.countBySessaoVotacaoIdAndVoteValue(sessaoVotacaoId, VotoValue.NAO);
        long totalVotes = votesYes + votesNo;

        VotingResultDTO.Result result = VotingResultDTO.determineResult(votesYes, votesNo);

        return new VotingResultDTO(sessaoVotacaoId, totalVotes, votesYes, votesNo, result.toString());
    }
}




