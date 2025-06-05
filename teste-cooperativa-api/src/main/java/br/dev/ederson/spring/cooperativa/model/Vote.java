package br.dev.ederson.spring.cooperativa.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vote_seq")
    @SequenceGenerator(name = "vote_seq", sequenceName = "vote_seq", allocationSize = 1)
    private Long id;
    @OneToOne
    @JoinColumn(name = "associate_id")
    private Associate associate;
    @OneToOne
    @JoinColumn(name = "agenda_id")
    private Agenda agenda;
    @Column
    private boolean approve;
    @Column
    private LocalDateTime voteDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Agenda getAgenda() {
        return agenda;
    }

    public void setAgenda(Agenda agenda) {
        this.agenda = agenda;
    }

    public Associate getAssociate() {
        return associate;
    }

    public void setAssociate(Associate associate) {
        this.associate = associate;
    }

    public boolean isApprove() {
        return approve;
    }

    public void setApprove(boolean approve) {
        this.approve = approve;
    }

    public LocalDateTime getVoteDate() {
        return voteDate;
    }

    public void setVoteDate(LocalDateTime voteDate) {
        this.voteDate = voteDate;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Vote vote = (Vote) o;
        return approve == vote.approve && Objects.equals(id, vote.id) && Objects.equals(associate, vote.associate) && Objects.equals(agenda, vote.agenda) && Objects.equals(voteDate, vote.voteDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, associate, agenda, approve, voteDate);
    }

    @Override
    public String toString() {
        return "Vote{" +
                "approve=" + approve +
                ", id=" + id +
                ", associate=" + associate +
                ", agenda=" + agenda +
                ", voteDate=" + voteDate +
                '}';
    }
}
