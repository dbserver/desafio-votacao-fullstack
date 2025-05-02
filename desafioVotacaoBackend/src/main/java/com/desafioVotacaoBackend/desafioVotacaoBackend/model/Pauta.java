package com.desafioVotacaoBackend.desafioVotacaoBackend.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pauta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String descricao;

    @OneToMany(mappedBy = "pauta", cascade = CascadeType.ALL)
    private List<Voto> votos;

    private LocalDateTime dataAbertura;
    private LocalDateTime dataFechamento;

    private Boolean sessaoAtiva = false;

    public Pauta(String titulo, String descricao) {
        this.titulo = titulo;
        this.descricao = descricao;
    }

    public void abrirSessao(Integer minutos) {
        this.dataAbertura = LocalDateTime.now();
        this.dataFechamento = this.dataAbertura.plusMinutes(minutos != null ? minutos : 1);
        this.sessaoAtiva = true;
    }

    public boolean isSessaoAtiva() {
        return sessaoAtiva && LocalDateTime.now().isBefore(dataFechamento);
    }
}