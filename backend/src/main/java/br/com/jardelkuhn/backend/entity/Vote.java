package br.com.jardelkuhn.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(
        indexes = {
                @Index(name = "idx_vote_agenda", columnList = "agenda_id")
        },
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"agenda_id", "associateId"})
        }
)
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "agenda_id", nullable = false)
    private Agenda agenda;

    @Column(nullable = false)
    private String associateId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VoteOption voteOption;

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

    public String getAssociateId() {
        return associateId;
    }

    public void setAssociateId(String associateId) {
        this.associateId = associateId;
    }

    public VoteOption getVoteOption() {
        return voteOption;
    }

    public void setVoteOption(VoteOption voteOption) {
        this.voteOption = voteOption;
    }
}