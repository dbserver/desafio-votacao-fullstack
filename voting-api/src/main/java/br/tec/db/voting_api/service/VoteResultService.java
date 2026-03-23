package br.tec.db.voting_api.service;

import br.tec.db.voting_api.domain.Agenda;
import br.tec.db.voting_api.domain.enums.VowType;
import br.tec.db.voting_api.dto.output.VoteResultOutputDTO;
import br.tec.db.voting_api.exception.BusinessException;
import br.tec.db.voting_api.repository.AgendaRepository;
import br.tec.db.voting_api.repository.VowRepository;
import br.tec.db.voting_api.repository.projection.VotingResultProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VoteResultService {

    private final AgendaRepository agendaRepository;
    private final VowRepository vowRepository;

    public VoteResultOutputDTO getVowsResults(Long agendaId) {
        Agenda agenda = agendaRepository.findById(agendaId)
                .orElseThrow(() -> new BusinessException("Pauta não encontrada"));

        long numberOfVotes = vowRepository.countVotes(agendaId);

        VotingResultProjection results = vowRepository.countResult(agendaId);

        long totalYes = results.getTotalYes() != null ? results.getTotalYes() : 0L;
        long totalNo = results.getTotalNo() != null ? results.getTotalNo() : 0L;

        String result = totalYes > totalNo ? "Sim" : totalYes < totalNo ? "Não" : "Empate";

        return new VoteResultOutputDTO(agendaId, totalYes, totalNo, numberOfVotes, result);
    }
}
