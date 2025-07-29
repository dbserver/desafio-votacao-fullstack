package com.nunesd66.pautas_api.voto.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.nunesd66.pautas_api.enumerations.SimNaoEnum;
import com.nunesd66.pautas_api.sessaoVotacao.model.SessaoVotacao;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "voto")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Voto implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String cpfAssociado;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SimNaoEnum voto;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "sessao_votacao_id", nullable = false)
    @ToString.Exclude @EqualsAndHashCode.Exclude
    private SessaoVotacao sessaoVotacao;

}
