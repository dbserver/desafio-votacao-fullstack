package com.nunesd66.pautas_api.sessaoVotacao.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.nunesd66.pautas_api.pauta.model.Pauta;
import com.nunesd66.pautas_api.voto.model.Voto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sessao_votacao")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SessaoVotacao implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column()
    private Integer duracao;

    @Column(nullable = false, columnDefinition = "DATETIME")
    LocalDateTime dataAbertura;

    @Column(nullable = false, columnDefinition = "DATETIME")
    LocalDateTime dataFechamento;

    @JsonBackReference
    @OneToOne
    @JoinColumn(name = "pauta", nullable = false)
    private Pauta pauta;

    @JsonManagedReference
    @OneToMany(mappedBy = "sessaoVotacao", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Voto> votos = new ArrayList<>();

    @PrePersist
    private void before() {
        this.dataAbertura = LocalDateTime.now(ZoneId.of("America/Sao_Paulo"));

        if (duracao == null) {
            duracao = 1;
        }

        this.dataFechamento = this.dataAbertura.plusMinutes(this.duracao);
    }

}
