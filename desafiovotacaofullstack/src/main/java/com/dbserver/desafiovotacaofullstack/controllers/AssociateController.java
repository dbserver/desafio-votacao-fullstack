package com.dbserver.desafiovotacaofullstack.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dbserver.desafiovotacaofullstack.domains.Associate;
import com.dbserver.desafiovotacaofullstack.dtos.AssociateRequestDto;
import com.dbserver.desafiovotacaofullstack.dtos.AssociateResponseDto;
import com.dbserver.desafiovotacaofullstack.services.AssociateService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/associate")
public class AssociateController {
	
	@Autowired
	AssociateService associateService;
	
	@PostMapping
	public ResponseEntity<AssociateResponseDto> createAssociate(@Valid @RequestBody AssociateRequestDto associateRequestDto) {
		return new ResponseEntity<>(associateService.createAssociate(associateRequestDto), HttpStatus.CREATED);
	}
		
	@GetMapping
	public ResponseEntity<List<Associate>> getAllVotesBySession(){
		return new ResponseEntity<>(associateService.getAllAssociates(),HttpStatus.OK) ;
	}
}
