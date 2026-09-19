package com.example.votacao.sessao;

import java.time.Duration;
import java.time.Instant;

import com.example.votacao.pauta.Pauta;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "sessao")
public class Sessao {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "pauta_id", nullable = false, unique = true)
  private Pauta pauta;

  @Column(nullable = false)
  private Instant inicio;

  @Column(nullable = false)
  private Instant fim;

  protected Sessao() {
  }

  public Sessao(Pauta pauta, Duration duracao) {
    this.pauta = pauta;
    this.inicio = Instant.now();
    this.fim = inicio.plus(duracao);
  }

  public boolean isAberta() {
    return Instant.now().isBefore(fim);
  }

  public Long getId() {
    return id;
  }

  public Instant getInicio() {
    return inicio;
  }

  public Instant getFim() {
    return fim;
  }

  public Pauta getPauta() {
    return pauta;
  }

}
