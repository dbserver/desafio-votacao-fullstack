package com.dbchal.voting.domain.entity;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "pautas")
public class Pauta {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String titulo;

	@Column(length = 1000)
	private String descricao;

	@Column(name = "dth_criacao", nullable = false)
	private Instant dthCriacao;

	protected Pauta() {
	}

	public Pauta(String titulo, String descricao) {
		this.titulo = titulo;
		this.descricao = descricao;
		this.dthCriacao = Instant.now();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Instant getDthCriacao() {
		return dthCriacao;
	}

	public void setDthCriacao(Instant dthCriacao) {
		this.dthCriacao = dthCriacao;
	}

}
