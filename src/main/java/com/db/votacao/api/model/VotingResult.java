package com.db.votacao.api.model;

public class VotingResult {
    private long yesVotes;
    private long noVotes;

    public VotingResult() {
    }

    public VotingResult(long yesVotes, long noVotes) {
        this.yesVotes = yesVotes;
        this.noVotes = noVotes;
    }

    public long getYesVotes() {
        return yesVotes;
    }

    public void setYesVotes(long yesVotes) {
        this.yesVotes = yesVotes;
    }

    public long getNoVotes() {
        return noVotes;
    }

    public void setNoVotes(long noVotes) {
        this.noVotes = noVotes;
    }

    public long getTotalVotes() {
        return yesVotes + noVotes;
    }
}
