package br.dev.ederson.spring.cooperativa.model;

import br.dev.ederson.spring.cooperativa.enumerador.AgendaStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Agenda {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "agenda_seq")
    @SequenceGenerator(name = "agenda_seq", sequenceName = "agenda_seq", allocationSize = 1)
    private Long id;
    @Column
    private String description;
    @Column(length = 1)
    @Enumerated(EnumType.ORDINAL)
    private AgendaStatus status;
    @Column
    private LocalDateTime registrationDate;
    @Column
    private LocalDateTime resultDate;
    @ManyToOne
    @JoinColumn(name = "session_id")
    private Session session;


    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public AgendaStatus getStatus() {
        return status;
    }

    public void setStatus(AgendaStatus status) {
        this.status = status;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }

    public LocalDateTime getResultDate() {
        return resultDate;
    }

    public void setResultDate(LocalDateTime resultDate) {
        this.resultDate = resultDate;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Agenda agenda = (Agenda) o;
        return Objects.equals(id, agenda.id) && Objects.equals(description, agenda.description) && status == agenda.status && Objects.equals(registrationDate, agenda.registrationDate) && Objects.equals(resultDate, agenda.resultDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, status, registrationDate, resultDate);
    }

    @Override
    public String toString() {
        return "Agenda{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", registrationDate=" + registrationDate +
                ", resultDate=" + resultDate +
                '}';
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }
}
