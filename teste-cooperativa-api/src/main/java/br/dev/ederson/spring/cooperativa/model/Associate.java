package br.dev.ederson.spring.cooperativa.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class Associate {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "associate_seq")
    @SequenceGenerator(name = "associate_seq", sequenceName = "associate_seq", allocationSize = 1)
    private Long id;
    @Column(name="name", length = 100)
    private String name;
    @Column(name="document", length = 14)
    private String document;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String nome) {
        this.name = nome;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Associate associate = (Associate) o;
        return Objects.equals(id, associate.id) && Objects.equals(name, associate.name) && Objects.equals(document, associate.document);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, document);
    }

    @Override
    public String toString() {
        return "Associate{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", document='" + document + '\'' +
                '}';
    }
}
