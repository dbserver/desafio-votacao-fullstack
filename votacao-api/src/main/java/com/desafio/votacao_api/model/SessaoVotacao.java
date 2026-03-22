package com.desafio.votacao_api.model;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents a voting session associated with a specific agenda (pauta).
 *
 * <p>A session starts at creation time and remains open until
 * {@code finalSessionDateTime}.</p>
 */
@Entity
@Table(name = "sessao_votacao")
public class SessaoVotacao {

    private static final long DEFAULT_DURATION_SECONDS = 60L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pauta_id", nullable = false)
    private Pauta pauta;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "final_session_date_time", nullable = false)
    private Instant finalSessionDateTime;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    /**
     * JPA constructor.
     */
    protected SessaoVotacao() {}

    /**
     * Opens a voting session for the given agenda.
     *
     * <p>If durationInSeconds is less than or equal to zero, the default duration (60 seconds) is used.</p>
     *
     * @param pauta the agenda linked to this session
     * @param durationInSeconds session duration in seconds
     * @return a fully initialized voting session
     */
    public static SessaoVotacao openSession(Pauta pauta, int durationInSeconds) {
        SessaoVotacao sessao = new SessaoVotacao();
        Instant now = Instant.now();
        long effectiveDuration = durationInSeconds > 0 ? durationInSeconds : DEFAULT_DURATION_SECONDS;

        sessao.pauta = pauta;
        sessao.createdAt = now;
        sessao.updatedAt = now;
        sessao.finalSessionDateTime = now.plusSeconds(effectiveDuration);
        sessao.isActive = true;
        return sessao;
    }

    /**
     * Checks whether the session is still open at the current instant.
     *
     * @return true when current time is before finalSessionDateTime
     */
    public boolean isOpen() {
        Instant now = Instant.now();
        return now.isBefore(finalSessionDateTime);
    }


    /**
     * Returns the unique identifier of this voting session.
     *
     * @return voting session identifier
     */
    public UUID getId() {
        return id;
    }

    /**
     * Returns the agenda associated with this voting session.
     *
     * @return associated agenda
     */
    public Pauta getPauta() {
        return pauta;
    }

    /**
     * Returns the session creation timestamp.
     *
     * @return instant when the session was opened
     */
    public Instant getCreatedAt() {
        return createdAt;
    }

    /**
     * Returns the session end timestamp.
     *
     * @return the instant when the voting session closes
     */
    public Instant getFinalSessionDateTime() {
        return finalSessionDateTime;
    }

    /**
     * Updates the session end timestamp.
     *
     * @param finalDateTime the new session end timestamp
     */
    public void setFinalSessionDateTime(Instant finalDateTime) {
        this.finalSessionDateTime = finalDateTime;
    }

    /**
     * Returns the last update timestamp of this session.
     *
     * @return instant of the last update
     */
    public Instant getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Checks whether the session is active for voting.
     *
     * @return true if active, false otherwise
     */
    public boolean isActive() {
        return this.isActive;
    }

    /**
     * Sets whether the session is active for voting.
     *
     * @param active true to activate, false to deactivate
    */
    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SessaoVotacao that = (SessaoVotacao) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }


    /**
     * Initializes timestamps before the first persistence.
     */
    @PrePersist
    void onCreate() {
        Instant now = Instant.now();
        if (this.createdAt == null) {
            this.createdAt = now;
        }
        this.updatedAt = now;
    }


    /**
     * Updates the last modification timestamp before persisting updates.
     */
    @PreUpdate
    void onUpdate() {
        this.updatedAt = Instant.now();
    }
}
