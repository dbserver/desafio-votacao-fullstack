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
import com.dbserver.desafiovotacaofullstack.services.AssociateService;

@RestController
@RequestMapping("/api/associate")
public class AssociateController {
	
	@Autowired
	AssociateService associateService;
	
	@PostMapping
	public ResponseEntity<Associate> createAssociate(@RequestBody Associate associate) {
		return new ResponseEntity<>(associateService.createAssociate(associate), HttpStatus.CREATED);
	}
		
	@GetMapping
	public ResponseEntity<List<Associate>> getAllVotesBySession(){
		return new ResponseEntity<>(associateService.getAllAssociates(),HttpStatus.OK) ;
	}
}
