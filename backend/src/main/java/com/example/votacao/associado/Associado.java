package com.example.votacao.associado;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "associado")
public class Associado {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 255)
  private String nome;

  @Column(nullable = false, length = 11, unique = true)
  private String cpf;

  protected Associado() {
  }

  public Associado(String nome, String cpf) {
    this.nome = nome;
    this.cpf = somenteDigitos(cpf);
  }

  public static String somenteDigitos(String cpf) {
    return cpf.replaceAll("\\D", "");
  }

  public Long getId() {
    return id;
  }

  public String getNome() {
    return nome;
  }

  public String getCpf() {
    return cpf;
  }
}
