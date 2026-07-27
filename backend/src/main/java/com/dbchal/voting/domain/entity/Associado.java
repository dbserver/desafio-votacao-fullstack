package com.dbchal.voting.domain.entity;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "associados")
public class Associado {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true, length = 11)
	private String cpf;

	@Column(name = "dth_criacao", nullable = false)
	private Instant dthCriacao;

	protected Associado() {
	}

	public Associado(String cpf) {
		this.cpf = cpf;
		this.dthCriacao = Instant.now();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public Instant getDthCriacao() {
		return dthCriacao;
	}

	public void setDthCriacao(Instant dthCriacao) {
		this.dthCriacao = dthCriacao;
	}

}
