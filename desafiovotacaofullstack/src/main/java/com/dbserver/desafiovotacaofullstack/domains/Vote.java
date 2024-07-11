package com.dbserver.desafiovotacaofullstack.domains;

import java.io.Serializable;

import com.dbserver.desafiovotacaofullstack.dtos.VoteResponseDto;
import com.dbserver.desafiovotacaofullstack.embeddables.SessionAssociateId;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "vote")
public class Vote implements Serializable {
	
	private static final long serialVersionUID = 1L;
	@EmbeddedId
    private SessionAssociateId id;
	private String response;
	
	public VoteResponseDto entityToDto() {
		return new VoteResponseDto(id.getAssociate().entityToDto(), response);
	}

	public Vote(Session session, Associate associate, String response) {
		this.id = new SessionAssociateId(session, associate);
		this.response = response;
	}
	
}
