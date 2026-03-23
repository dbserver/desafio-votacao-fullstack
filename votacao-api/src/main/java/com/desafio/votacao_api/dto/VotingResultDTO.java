package com.desafio.votacao_api.dto;

import java.util.UUID;

/**
 * Data Transfer Object for voting session results.
 *
 * Contains the vote count for a specific voting session, showing the number of
 * affirmative (SIM) and negative (NAO) votes.
 *
 * @param sessaoVotacaoId unique identifier of the voting session
 * @param totalVotes total number of votes cast
 * @param votesYes count of affirmative votes (SIM)
 * @param votesNo count of negative votes (NAO)
 * @param result the outcome of the vote (APPROVED, REJECTED, or TIED)
 */
public record VotingResultDTO(
        UUID sessaoVotacaoId,
        long totalVotes,
        long votesYes,
        long votesNo,
        String result
) {
    /**
     * Enum for voting results.
     */
    public enum Result {
        /**
         * Affirmative votes exceed negative votes.
         */
        APPROVED,

        /**
         * Negative votes exceed affirmative votes.
         */
        REJECTED,

        /**
         * Affirmative and negative votes are equal.
         */
        TIED
    }

    /**
     * Determines the result based on vote counts.
     *
     * @param votesYes count of affirmative votes
     * @param votesNo count of negative votes
     * @return the corresponding Result enum
     */
    public static Result determineResult(long votesYes, long votesNo) {
        if (votesYes > votesNo) {
            return Result.APPROVED;
        } else if (votesNo > votesYes) {
            return Result.REJECTED;
        } else {
            return Result.TIED;
        }
    }
}

