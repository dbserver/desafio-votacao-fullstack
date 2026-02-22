package dev.rodrigo.desafiovotacao.entity;

import dev.rodrigo.desafiovotacao.enums.ResultadoVotacao;
import dev.rodrigo.desafiovotacao.enums.StatusSessao;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "sessao_votacao")
public class SessaoVotacao {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(optional = false)
  @JoinColumn(name = "pauta_id")
  private Pauta pauta;

  @Column(nullable = false)
  private LocalDateTime dataAbertura;

  @Column(nullable = false)
  private LocalDateTime dataFechamento;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private StatusSessao status;

  @Enumerated(EnumType.STRING)
  private ResultadoVotacao resultado;

  @OneToMany(mappedBy = "sessao", cascade = CascadeType.ALL)
  private List<Voto> votos;

  public boolean isExpirada() {
    return LocalDateTime.now().isAfter(dataFechamento);
  }

  public boolean isAberta() {
    return status == StatusSessao.ABERTA;
  }

  public void encerrar(ResultadoVotacao resultado) {
    this.status = StatusSessao.ENCERRADA;
    this.resultado = resultado;
  }
}
