package com.dbserver.desafiovotacaofullstack.embeddables;

import java.io.Serializable;
import java.util.Objects;

import com.dbserver.desafiovotacaofullstack.domains.Associate;
import com.dbserver.desafiovotacaofullstack.domains.Session;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SessionAssociateId implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@ManyToOne
    @JoinColumn(name = "id_session")
	private Session session;
	
	@ManyToOne
	@JoinColumn(name = "id_associate")
    private Associate associate;
	
	@Override
	public int hashCode() {
		return Objects.hash(associate, session);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SessionAssociateId other = (SessionAssociateId) obj;
		return Objects.equals(associate, other.associate) && Objects.equals(session, other.session);
	}
    
}
