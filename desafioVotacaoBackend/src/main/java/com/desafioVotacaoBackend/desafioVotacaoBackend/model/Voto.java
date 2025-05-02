package com.desafioVotacaoBackend.desafioVotacaoBackend.model;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Voto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Pauta pauta;

    @Column(length = 11)
    private String cpf;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private VotoOpcao opcao;

    private LocalDateTime dataVoto;

    @PrePersist
    protected void onCreate() {
        this.dataVoto = LocalDateTime.now();
    }

    public enum VotoOpcao {
        SIM,
        NAO
    }

}
