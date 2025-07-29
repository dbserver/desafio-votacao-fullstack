package com.nunesd66.pautas_api.pauta.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.nunesd66.pautas_api.sessaoVotacao.model.SessaoVotacao;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "pauta")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Pauta implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String descricao;

    @JsonManagedReference
    @OneToOne(mappedBy = "pauta", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private SessaoVotacao sessaoVotacao;

}
