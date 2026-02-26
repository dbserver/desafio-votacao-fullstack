package db.server.app.domain.Rulling.Model;

import com.mongodb.lang.Nullable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document( collection = "rulling" )
public class Rulling {
    @Id
    private String id;

    private String title;

    @Nullable
    private String description;

    private String author;
    private Date expiration;
    private Integer voteNo;
    private Integer voteYes;
    private Integer voteTotal;
    private Boolean active;
    private Date createdAt;

    public void setNewRulling(String title, String description, String author, Date expiration) {
        this.title = title;
        this.description = description;
        this.author = author;
        this.expiration = expiration;
        this.active = true;
        this.createdAt = new Date();
        this.voteNo = 0;
        this.voteYes = 0;
        this.voteTotal = 0;
    }

    public void SetActive(Boolean active) {
        this.active = active;
    }

    public void increaseVoteYes() {
        this.voteYes++;
        this.voteTotal++;
    }

    public void decreaseVoteYes() {
        this.voteYes--;
        this.voteTotal--;
    }

    public void increaseVoteNo() {
        this.voteNo++;
        this.voteTotal++;
    }

    public void decreaseVoteNo() {
        this.voteNo--;
        this.voteTotal--;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setExpiration(Date expiration) {
        this.expiration = expiration;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    @Nullable
    public String getDescription() {
        return description;
    }

    public String getAuthor() {
        return author;
    }

    public Date getExpiration() {
        return expiration;
    }

    public Integer getVoteNo() {
        return voteNo;
    }

    public Integer getVoteYes() {
        return voteYes;
    }

    public Integer getVoteTotal() {
        return voteTotal;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Boolean getActive() {
        return active;
    }
}
