package com.votacao.backend.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.votacao.backend.infrastructure.time.AppDateTime;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "sessao",
        indexes = {
                @Index(name = "idx_sessao_pauta_id", columnList = "pauta_id"),
                @Index(name = "idx_sessao_encerrada_expira", columnList = "encerrada_em,expira_em")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class Sessao {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pauta_id", nullable = false)
    private Pauta pauta;

    @Column(name = "aberta_em", nullable = false, updatable = false)
    private LocalDateTime abertaEm = AppDateTime.now();

    @Column(name = "expira_em", nullable = false)
    private LocalDateTime expiraEm;

    @Column(name = "encerrada_em")
    private LocalDateTime encerradaEm;

    public boolean isAtiva() {
        return encerradaEm == null && AppDateTime.now().isBefore(expiraEm);
    }
}
