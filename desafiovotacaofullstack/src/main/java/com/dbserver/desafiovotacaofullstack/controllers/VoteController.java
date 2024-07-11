package com.dbserver.desafiovotacaofullstack.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dbserver.desafiovotacaofullstack.dtos.TotalVotesDto;
import com.dbserver.desafiovotacaofullstack.dtos.VoteRequestDto;
import com.dbserver.desafiovotacaofullstack.dtos.VoteResponseDto;
import com.dbserver.desafiovotacaofullstack.services.VoteService;

@RestController
@RequestMapping("/api/votes")
public class VoteController {
	
	@Autowired
	VoteService voteService;
	
	@PostMapping
	public ResponseEntity<VoteResponseDto> createVote(@RequestBody VoteRequestDto voteRequestDto) {
		return new ResponseEntity<>(voteService.createVote(voteRequestDto), HttpStatus.CREATED);
	}
		
	@GetMapping("/session/{id}")
	public ResponseEntity<TotalVotesDto> getAllVotesBySession(@PathVariable("id") Integer idSession){
		return new ResponseEntity<>(voteService.getAllVotesBySession(idSession),HttpStatus.OK) ;
	}
}
