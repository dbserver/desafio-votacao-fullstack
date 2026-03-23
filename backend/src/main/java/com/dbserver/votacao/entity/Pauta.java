package com.dbserver.votacao.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "pautas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pauta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    private String descricao;

    @Column(name = "criada_em", nullable = false)
    private LocalDateTime criadaEm;

    @PrePersist
    public void prePersist() {
        criadaEm = LocalDateTime.now();
    }
}
