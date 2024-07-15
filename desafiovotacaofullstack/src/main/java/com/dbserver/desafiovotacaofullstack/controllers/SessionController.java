package com.dbserver.desafiovotacaofullstack.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dbserver.desafiovotacaofullstack.dtos.SessionRequestDto;
import com.dbserver.desafiovotacaofullstack.dtos.SessionResponseDto;
import com.dbserver.desafiovotacaofullstack.services.SessionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/sessions")
public class SessionController {
	
	@Autowired
	SessionService sessionService;
	
	@PostMapping
	public ResponseEntity<SessionResponseDto> createSession(@Valid @RequestBody SessionRequestDto sessionRequestDto){
		return new ResponseEntity<>(sessionService.createSession(sessionRequestDto), HttpStatus.CREATED);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<SessionResponseDto> createSession(@Valid @PathVariable Integer id, @RequestBody SessionRequestDto sessionRequestDto){
		return new ResponseEntity<>(sessionService.updateSession(id, sessionRequestDto), HttpStatus.OK);
	}
	
	@GetMapping("/{id}/isOpen")
    public ResponseEntity<Boolean> isSessionOpen(@PathVariable Integer id) {
        boolean isOpen = sessionService.isSessionOpen(id);
        return new ResponseEntity<>(isOpen, HttpStatus.OK);
    }
}
