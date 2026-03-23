package com.desafio.votacao_api.model;

import com.desafio.votacao_api.model.enums.VotoValue;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

/**
 * Entity that represents a vote cast by a member in a voting session.
 *
 * <p>A vote is uniquely identified by its ID and constrained by session and CPF
 * to avoid duplicate votes from the same member in the same session.</p>
 */
@Entity
@Table(name = "voto", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"sessao_id", "cpf"})
}, indexes = {
        @Index(name = "idx_voto_sessao", columnList = "sessao_id")
})
public class Voto {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sessao_id",  nullable = false)
    private SessaoVotacao sessaoVotacao;

    @Embedded
    private Cpf cpf;

    @Enumerated(EnumType.STRING)
    @Column(name = "vote", nullable = false,  length = 3)
    private VotoValue vote;
    
    @Column(name = "is_active", nullable = false)
    private boolean isActive;
    
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;
    
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    /**
     * Default constructor required by JPA.
     */
    public Voto() {}

    /**
     * Creates a new vote for a session.
     *
     * @param sessaoVotacao voting session where the vote is registered
     * @param vote vote value (YES/NO)
     * @param cpf member CPF
     */
    public Voto(SessaoVotacao sessaoVotacao, VotoValue vote, Cpf cpf) {
        this.sessaoVotacao = sessaoVotacao;
        this.vote = vote;
        this.cpf = cpf;
        this.isActive = true;
    }

    /**
     * Initializes audit timestamps before first persistence.
     */
    @PrePersist
    private void onCreate() {
        Instant now = Instant.now();
        if (this.createdAt == null) {
            this.createdAt = now;
        }
        this.updatedAt = now;
    }

    /**
     * Updates the audit timestamp before entity update.
     */
    @PreUpdate
    private void onUpdate() {
        this.updatedAt = Instant.now();
    }

    /**
     * Returns the vote identifier.
     *
     * @return vote ID
     */
    public UUID getId() {
        return id;
    }

    /**
     * Returns the associated voting session.
     *
     * @return voting session
     */
    public SessaoVotacao getSessaoVotacao() {
        return sessaoVotacao;
    }

    /**
     * Sets the associated voting session.
     *
     * @param sessaoVotacao voting session
     */
    public void setSessaoVotacao(SessaoVotacao sessaoVotacao) {
        this.sessaoVotacao = sessaoVotacao;
    }

    /**
     * Returns the member CPF.
     *
     * @return member CPF
     */
    public Cpf getCpf() {
        return cpf;
    }

    /**
     * Sets the member CPF.
     *
     * @param cpf member CPF
     */
    public void setCpf(Cpf cpf) {
        this.cpf = cpf;
    }

    /**
     * Returns the vote value.
     *
     * @return vote value
     */
    public VotoValue getVote() {
        return vote;
    }

    /**
     * Sets the vote value.
     *
     * @param vote vote value
     */
    public void setVote(VotoValue vote) {
        this.vote = vote;
    }

    /**
     * Indicates whether this vote is active.
     *
     * @return {@code true} when active, otherwise {@code false}
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * Defines whether this vote is active.
     *
     * @param active active flag
     */
    public void setActive(boolean active) {
        isActive = active;
    }

    /**
     * Returns the creation timestamp.
     *
     * @return creation instant
     */
    public Instant getCreatedAt() {
        return createdAt;
    }

    /**
     * Returns the last update timestamp.
     *
     * @return update instant
     */
    public Instant getUpdatedAt() {
        return updatedAt;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Voto voto = (Voto) o;
        return id != null && id.equals(voto.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
