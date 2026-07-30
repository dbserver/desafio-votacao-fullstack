package br.com.jardelkuhn.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class Agenda {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private LocalDateTime openedDate;
    private LocalDateTime limitDate;

    public static class Builder
    {
        private String title;
        private String description;
        private LocalDateTime openedDate;
        private LocalDateTime limitDate;

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setOpenedDate(LocalDateTime openedDate) {
            this.openedDate = openedDate;
            return this;
        }

        public Builder setLimitDate(LocalDateTime limitDate) {
            this.limitDate = limitDate;
            return this;
        }

        public Agenda build()
        {
            return new Agenda(this);
        }
    }

    public Agenda(){}

    private Agenda(Builder builder)
    {
        title = builder.title;
        description = builder.description;
        openedDate = builder.openedDate;
        limitDate = builder.limitDate;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getLimitDate() {
        return limitDate;
    }

    public void setLimitDate(LocalDateTime limitDate) {
        this.limitDate = limitDate;
    }

    public LocalDateTime getOpenedDate() {
        return openedDate;
    }

    public void setOpenedDate(LocalDateTime openedDate) {
        this.openedDate = openedDate;
    }
}