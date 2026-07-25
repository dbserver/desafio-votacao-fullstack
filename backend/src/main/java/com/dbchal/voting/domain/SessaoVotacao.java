package com.dbchal.voting.domain;

import java.time.Instant;

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
@Table(name = "sessao_votacao")
public class SessaoVotacao {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "pauta_id", nullable = false, unique = true)
	private Pauta pauta;

	@Column(name = "dth_inicio", nullable = false)
	private Instant dthInicio;

	@Column(name = "dth_fim", nullable = false)
	private Instant dthFim;

	protected SessaoVotacao() {
	}

	public SessaoVotacao(Long id, Pauta pauta, Instant dthInicio, Instant dthFim) {
		this.pauta = pauta;
		this.dthInicio = dthInicio;
		this.dthFim = dthFim;
	}

	public boolean isOpen() {
		return Instant.now().isBefore(dthFim);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Pauta getPauta() {
		return pauta;
	}

	public void setPauta(Pauta pauta) {
		this.pauta = pauta;
	}

	public Instant getDthInicio() {
		return dthInicio;
	}

	public void setDthInicio(Instant dthInicio) {
		this.dthInicio = dthInicio;
	}

	public Instant getDthFim() {
		return dthFim;
	}

	public void setDthFim(Instant dthFim) {
		this.dthFim = dthFim;
	}

}
