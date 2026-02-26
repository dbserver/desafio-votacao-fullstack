package db.server.app.domain.Rulling.Model;

import db.server.app.commons.Enums.Vote;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "rulling_vote")
public class RullingVote {
    @Id
    private String id;

    private String rullingId;
    private String userId;
    private Vote vote;
    private Date createdAt;

    public void setNewVote(String rullingId, String userId, Vote vote) {
        this.rullingId = rullingId;
        this.userId = userId;
        this.vote = vote;
        this.createdAt = new Date();
    }

    public String getId() {
        return id;
    }

    public String getRullingId() {
        return rullingId;
    }

    public String getUserId() {
        return userId;
    }

    public Vote getVote() {
        return vote;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

}
