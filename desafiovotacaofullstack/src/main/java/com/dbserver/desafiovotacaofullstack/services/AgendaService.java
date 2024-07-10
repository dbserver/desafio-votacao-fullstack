package com.dbserver.desafiovotacaofullstack.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dbserver.desafiovotacaofullstack.domains.Agenda;
import com.dbserver.desafiovotacaofullstack.dtos.AgendaRequestDto;
import com.dbserver.desafiovotacaofullstack.dtos.AgendaResponseDto;
import com.dbserver.desafiovotacaofullstack.repositories.AgendaRepository;

@Service
public class AgendaService {
	
	@Autowired
	AgendaRepository agendaRepository;
	
	public AgendaResponseDto createAgenda(AgendaRequestDto agendaRequestDto) {
		return agendaRepository.save(new Agenda(agendaRequestDto.description())).entityToDto();
	}
	
	public AgendaResponseDto updateAgenda(Integer id, AgendaRequestDto agendaRequestDto) {
		if(!agendaRepository.existsById(id)) 
	         throw new RuntimeException("Agenda not found");
		
		Agenda agenda = agendaRepository.findById(id).get();
		agenda.setDescription(agendaRequestDto.description());
		return agendaRepository.save(agenda).entityToDto();
	}
	
	public void deleteAgenda(Integer id) {
        if (!agendaRepository.existsById(id)) {
            throw new RuntimeException("Agenda not found");
        }
        agendaRepository.deleteById(id);
    }
	
	public AgendaResponseDto getAgendaById(Integer id) {
        Agenda agenda = agendaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Agenda not found"));
        return agenda.entityToDto();
    }
	
	public List<AgendaResponseDto> getAllAgendas() {
        return agendaRepository.findAll().stream().map(agenda -> agenda.entityToDto()).collect(Collectors.toList());
    }
}
