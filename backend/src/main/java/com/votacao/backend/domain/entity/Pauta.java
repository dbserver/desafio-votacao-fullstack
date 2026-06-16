package com.votacao.backend.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.votacao.backend.infrastructure.time.AppDateTime;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "pauta")
@Getter
@Setter
@NoArgsConstructor
public class Pauta {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String titulo;

    private String descricao;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm = AppDateTime.now();
}
