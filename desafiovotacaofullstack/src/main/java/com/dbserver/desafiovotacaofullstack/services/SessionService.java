package com.dbserver.desafiovotacaofullstack.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dbserver.desafiovotacaofullstack.domains.Agenda;
import com.dbserver.desafiovotacaofullstack.domains.Session;
import com.dbserver.desafiovotacaofullstack.dtos.SessionRequestDto;
import com.dbserver.desafiovotacaofullstack.dtos.SessionResponseDto;
import com.dbserver.desafiovotacaofullstack.repositories.AgendaRepository;
import com.dbserver.desafiovotacaofullstack.repositories.SessionRepository;

@Service
public class SessionService {
	
	@Autowired
	private SessionRepository sessionRepository;
	
	@Autowired
	AgendaRepository agendaRepository;
	
	public SessionResponseDto createSession(SessionRequestDto sessionRequestDto) {
		Optional<Agenda> agenda = agendaRepository.findById(sessionRequestDto.idAgenda());
		if(agenda.isEmpty())
			throw new RuntimeException("Nenhuma sessão encontrada com esta pauta.");
		
		return sessionRepository.save(new Session(agenda.get(), sessionRequestDto.startTime(), sessionRequestDto.endTime() != null ? sessionRequestDto.endTime() : sessionRequestDto.startTime().plusMinutes(1))).entityToDto();
	}
	
	public SessionResponseDto updateSession(Integer id, SessionRequestDto sessionRequestDto) {
		
		Optional<Session> session = sessionRepository.findById(id);
		if(session.isEmpty())
			throw new RuntimeException("Sessão não encontrada.");
		
		Optional<Agenda> agenda = agendaRepository.findById(sessionRequestDto.idAgenda());
		if(agenda.isEmpty())
			throw new RuntimeException("Nenhuma sessão encontrada com esta pauta.");
		
		Session sessionAtualizado = session.get();
		sessionAtualizado.setStartTime(sessionRequestDto.startTime());
		sessionAtualizado.setEndTime(sessionRequestDto.endTime() != null ? sessionRequestDto.endTime() : sessionAtualizado.getEndTime());
		return sessionRepository.save(sessionAtualizado).entityToDto();
	}
	
	public boolean isSessionOpen(Integer id) {
        Session session = sessionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sessão não encontrada."));
        return session.isOpen();
    }
}
