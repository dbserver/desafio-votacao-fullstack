package com.dbserver.desafiovotacaofullstack.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dbserver.desafiovotacaofullstack.dtos.AgendaRequestDto;
import com.dbserver.desafiovotacaofullstack.dtos.AgendaResponseDto;
import com.dbserver.desafiovotacaofullstack.services.AgendaService;

@RestController
@RequestMapping("/api/agendas")
public class AgendaController {
	
	@Autowired
	private AgendaService agendaService;
	
	@PostMapping
	public ResponseEntity<AgendaResponseDto> createAgenda(@RequestBody AgendaRequestDto agendaRequestDto){
		return new ResponseEntity<>(agendaService.createAgenda(agendaRequestDto), HttpStatus.CREATED);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<AgendaResponseDto> updateAgenda(@PathVariable Integer id, @RequestBody AgendaRequestDto agendaRequestDto){
		return new ResponseEntity<>(agendaService.updateAgenda(id, agendaRequestDto), HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAgenda(@PathVariable Integer id) {
		agendaService.deleteAgenda(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
	
	@GetMapping("/{id}")
    public ResponseEntity<AgendaResponseDto> getPersonById(@PathVariable Integer id) {
        AgendaResponseDto agendaResponseDto = agendaService.getAgendaById(id);
        return new ResponseEntity<>(agendaResponseDto, HttpStatus.OK);
    }
	
	@GetMapping
    public ResponseEntity<List<AgendaResponseDto>> getAllAgendas() {
        List<AgendaResponseDto> agendas = agendaService.getAllAgendas();
        return new ResponseEntity<>(agendas, HttpStatus.OK);
    }
}
