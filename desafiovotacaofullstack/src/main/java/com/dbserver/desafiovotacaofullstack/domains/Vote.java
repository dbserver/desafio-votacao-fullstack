package com.dbserver.desafiovotacaofullstack.domains;

import java.io.Serializable;

import com.dbserver.desafiovotacaofullstack.embeddables.SessionAssociateId;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
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
@Table(name = "vote")
public class Vote implements Serializable {
	
	private static final long serialVersionUID = 1L;
	@EmbeddedId
    private SessionAssociateId id;
	private String response;
	
}
