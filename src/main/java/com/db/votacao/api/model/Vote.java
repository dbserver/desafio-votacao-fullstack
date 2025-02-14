package com.db.votacao.api.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(indexes = {
        @Index(name = "idx_vote_session_associate", columnList = "session_id,associate_id"),
        @Index(name = "idx_vote_session", columnList = "session_id")
})
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private VotingSession session;

    private String associateId;
    private boolean voteValue;

    public Vote() {
    }

    public Vote(VotingSession session, String associateId, boolean voteValue) {
        this.session = session;
        this.associateId = associateId;
        this.voteValue = voteValue;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public VotingSession getSession() {
        return session;
    }

    public void setSession(VotingSession session) {
        this.session = session;
    }

    public String getAssociateId() {
        return associateId;
    }

    public void setAssociateId(String associateId) {
        this.associateId = associateId;
    }

    public boolean isVoteValue() {
        return voteValue;
    }

    public void setVoteValue(boolean voteValue) {
        this.voteValue = voteValue;
    }
}