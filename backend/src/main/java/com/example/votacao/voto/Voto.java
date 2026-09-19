package com.example.votacao.voto;

import java.time.Instant;

import com.example.votacao.associado.Associado;
import com.example.votacao.sessao.Sessao;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "voto")
public class Voto {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "sessao_id", nullable = false)
  private Sessao sessao;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "associado_id", nullable = false)
  private Associado associado;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 3)
  private OpcaoVoto opcao;

  @Column(name = "criado_em", nullable = false)
  private Instant criadoEm;

  protected Voto() {
  }

  public Voto(Sessao sessao, Associado associado, OpcaoVoto opcao) {
    this.sessao = sessao;
    this.associado = associado;
    this.opcao = opcao;
    this.criadoEm = Instant.now();
  }

  public Long getId() {
    return id;
  }

  public Sessao getSessao() {
    return sessao;
  }

  public Associado getAssociado() {
    return associado;
  }

  public OpcaoVoto getOpcao() {
    return opcao;
  }

  public Instant getCriadoEm() {
    return criadoEm;
  }
}
