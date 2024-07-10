package com.dbserver.desafiovotacaofullstack.domains;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

import com.dbserver.desafiovotacaofullstack.dtos.SessionResponseDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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
@Table(name = "session")
public class Session implements Serializable {
	
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@OneToOne
	@JoinColumn(name = "id_agenda")
	private Agenda agenda;
	
	@Column(name = "start_time")
	private LocalDateTime startTime;
	
	@Column(name = "end_time")
    private LocalDateTime endTime;
	
	public boolean isOpen() {
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(startTime) && now.isBefore(endTime);
    }

    public boolean hasEnded() {
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(endTime);
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
		Session other = (Session) obj;
		return Objects.equals(id, other.id);
	}

	public Session(Agenda agenda, LocalDateTime startTime, LocalDateTime endTime) {
		this.agenda = agenda;
		this.startTime = startTime;
		this.endTime = endTime;
	}
	
	public Session(Agenda agenda, LocalDateTime startTime) {
		this.agenda = agenda;
		this.startTime = startTime;
		this.endTime = startTime.plusMinutes(1);
	}
	
	public SessionResponseDto entityToDto() {
		return new SessionResponseDto(agenda.getId(), startTime, endTime);
	}
	
}
