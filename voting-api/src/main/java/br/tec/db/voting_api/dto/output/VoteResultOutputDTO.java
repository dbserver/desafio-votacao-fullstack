package br.tec.db.voting_api.dto.output;

public record VoteResultOutputDTO(
        Long agendaId,
        Long totalYes,
        Long totalNo,
        Long numberOfVotes,
        String result
) { }
