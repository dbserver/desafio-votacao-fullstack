package com.desafio.votacao_api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Entity that represents a voting agenda.
 *
 * An agenda is a topic for which votes from members are intended to be collected.
 */
@Entity
@Table(name = "pauta")
public class Pauta {

    /** Unique identifier of the agenda. */
    @Id
    private UUID id;

    /** Name of the agenda. */
    @Column(name = "name", nullable = false)
    private String name;

    /** Detailed description of the agenda. */
    @Column(name = "description")
    private String description;

    /** Indicates whether the agenda is active for voting. */
    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    /** Date and time when the agenda was created. */
    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;

    /** Date and time of the last update of the agenda. */
    @Column(name = "updated_at", nullable = false)
    private ZonedDateTime updatedAt;

    protected Pauta() {}

    /**
     * Creates a new agenda with the provided name and description.
     *
     * @param name name of the agenda (cannot be null or empty)
     * @param description description of the agenda
     * @throws IllegalArgumentException if name is null or empty
     */
    public Pauta(String name, String description) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Nome da pauta não pode ser nulo ou vazio");
        }
        this.id = UUID.randomUUID();
        this.name = name;
        this.description = description;
        this.isActive = true;
        this.createdAt = ZonedDateTime.now();
        this.updatedAt = ZonedDateTime.now();
    }

    /**
     * Gets the unique identifier of the agenda.
     *
     * @return the agenda's ID
     */
    public UUID getId() {
        return id;
    }

    /**
     * Gets the name of the agenda.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the agenda.
     *
     * @param name new name (cannot be null or empty)
     * @throws IllegalArgumentException if name is null or empty
     */
    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Nome não pode ser nulo ou vazio");
        }
        this.name = name;
    }

    /**
     * Gets the description of the agenda.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the agenda.
     *
     * @param description the new description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Checks whether the agenda is active for voting.
     *
     * @return true if active, false otherwise
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * Sets whether the agenda is active for voting.
     *
     * @param active true to activate, false to deactivate
     */
    public void setActive(boolean active) {
        isActive = active;
    }

    /**
     * Gets the date and time when the agenda was created.
     *
     * @return the creation date and time
     */
    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Gets the date and time of the last update of the agenda.
     *
     * @return the last update date and time
     */
    public ZonedDateTime getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Sets the date and time of the last update of the agenda.
     *
     * @param updatedAt the new update date and time
     */
    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pauta pauta = (Pauta) o;
        return Objects.equals(id, pauta.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Pauta{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", isActive=" + isActive +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
