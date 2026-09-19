package com.example.votacao.pauta;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "pauta")
public class Pauta {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String titulo;

  @Column(nullable = false)
  private String descricao;

  private Instant criada_em;

  protected Pauta() {
  }

  public Pauta(String titulo, String descricao) {
    this.titulo = titulo;
    this.descricao = descricao;
    this.criada_em = Instant.now();
  }

  public Long getId() {
    return id;
  }

  public String getTitulo() {
    return titulo;
  }

  public String getDescricao() {
    return descricao;
  }

  public Instant getCriadaEm() {
    return criada_em;
  }

}
