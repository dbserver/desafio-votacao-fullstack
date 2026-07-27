package com.dbchal.voting.domain.entity;

import java.time.Instant;

import com.dbchal.voting.domain.enums.OpcaoVoto;

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
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "votos", uniqueConstraints = {
		@UniqueConstraint(name = "uk_voto_sessao_associado", columnNames = { "sessao_id", "associado_id" }) })
public class Voto {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "sessao_id", nullable = false)
	private SessaoVotacao sessao;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "associado_id", nullable = false)
	private Associado associado;

	@Enumerated(EnumType.STRING)
	@Column(name = "opcao", nullable = false, length = 3)
	private OpcaoVoto opcao;

	@Column(name = "dth_voto", nullable = false)
	private Instant dthVoto;

	protected Voto() {
	}

	public Voto(SessaoVotacao sessao, Associado associado, OpcaoVoto opcao) {
		this.sessao = sessao;
		this.associado = associado;
		this.opcao = opcao;
		this.dthVoto = Instant.now();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public SessaoVotacao getSessao() {
		return sessao;
	}

	public void setSessao(SessaoVotacao sessao) {
		this.sessao = sessao;
	}

	public Associado getAssociado() {
		return associado;
	}

	public void setAssociado(Associado associado) {
		this.associado = associado;
	}

	public OpcaoVoto getOpcao() {
		return opcao;
	}

	public void setOpcao(OpcaoVoto opcao) {
		this.opcao = opcao;
	}

	public Instant getDthVoto() {
		return dthVoto;
	}

	public void setDthVoto(Instant dthVoto) {
		this.dthVoto = dthVoto;
	}

}