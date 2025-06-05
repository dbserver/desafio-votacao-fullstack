package br.dev.ederson.spring.cooperativa.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "session_seq")
    @SequenceGenerator(name = "session_seq", sequenceName = "session_seq", allocationSize = 1)
    private Long id;
    @Column(nullable = false)
    private int durationTime;
    @Column
    private LocalDateTime startDate;
    @Column
    private LocalDateTime endDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getDurationTime() {
        return durationTime;
    }

    public void setDurationTime(int durationTime) {
        this.durationTime = durationTime;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Session session = (Session) o;
        return durationTime == session.durationTime && Objects.equals(id, session.id) && Objects.equals(startDate, session.startDate) && Objects.equals(endDate, session.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, durationTime, startDate, endDate);
    }

    @Override
    public String toString() {
        return "Session{" +
                "id=" + id +
                ", durationTime=" + durationTime +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }

}
