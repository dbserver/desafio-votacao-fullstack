package com.votacao.backend.domain.entity;

import com.votacao.backend.domain.enums.OpcaoVoto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.votacao.backend.infrastructure.time.AppDateTime;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "voto",
        indexes = {
                @Index(name = "idx_voto_sessao_opcao", columnList = "sessao_id,opcao")
        },
        uniqueConstraints = @UniqueConstraint(columnNames = {"sessao_id", "associado_id"})
)
@Getter
@Setter
@NoArgsConstructor
public class Voto {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sessao_id", nullable = false)
    private Sessao sessao;

    @Column(name = "associado_id", nullable = false)
    private String associadoId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OpcaoVoto opcao;

    @Column(name = "votado_em", nullable = false, updatable = false)
    private LocalDateTime votadoEm = AppDateTime.now();
}
