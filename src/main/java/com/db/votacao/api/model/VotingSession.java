package com.db.votacao.api.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class VotingSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Agenda agenda;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean isOpen;

    public boolean isSessionOpen() {
        LocalDateTime now = LocalDateTime.now();
        return isOpen && now.isBefore(endTime);
    }

    public VotingSession() {
    }

    public VotingSession(Agenda agenda, LocalDateTime startTime, LocalDateTime endTime) {
        this.agenda = agenda;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isOpen = true;
    }

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

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }
}