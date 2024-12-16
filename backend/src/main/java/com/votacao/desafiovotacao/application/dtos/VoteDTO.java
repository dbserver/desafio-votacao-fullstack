package com.votacao.desafiovotacao.application.dtos;


public record VoteDTO (String agendaId, String sessionId, String associatedId, String voteId, String vote) {
}
