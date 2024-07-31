package com.dbserver.desafiovotacaofullstack.domains;

import java.io.Serializable;
import java.util.Objects;

import com.dbserver.desafiovotacaofullstack.dtos.AssociateResponseDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "associate")
public class Associate implements Serializable {
	
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column
	private String name;
	@Column
	private String cpf;
	
	public AssociateResponseDto entityToDto() {
		return new AssociateResponseDto(id, name, cpf);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Associate other = (Associate) obj;
		return Objects.equals(id, other.id);
	}

	public Associate(String name, String cpf) {
		this.name = name;
		this.cpf = cpf;
	}
	
}
