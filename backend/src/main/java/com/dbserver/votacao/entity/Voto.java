package com.dbserver.votacao.entity;

import com.dbserver.votacao.enums.VotoOpcao;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "votos",
        uniqueConstraints = @UniqueConstraint(columnNames = {"sessao_id", "associado_id"}),
        indexes = @Index(name = "idx_voto_sessao_voto", columnList = "sessao_id, voto"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Voto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sessao_id", nullable = false)
    private SessaoVotacao sessao;

    @Column(name = "associado_id", nullable = false)
    private String associadoId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VotoOpcao voto;
}
