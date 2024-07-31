package com.dbserver.desafiovotacaofullstack.domains;

import com.dbserver.desafiovotacaofullstack.dtos.AgendaResponseDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "agenda")
public class Agenda {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column
	private String description;
	
	public Agenda(String description) {
		this.description = description;
	}
	
	public AgendaResponseDto entityToDto() {
		return new AgendaResponseDto(id, description);
	}
}
