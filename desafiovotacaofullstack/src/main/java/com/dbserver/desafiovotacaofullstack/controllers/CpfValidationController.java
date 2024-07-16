package com.dbserver.desafiovotacaofullstack.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dbserver.desafiovotacaofullstack.dtos.ResponseCpfValidationDto;
import com.dbserver.desafiovotacaofullstack.services.CpfValidationService;

@RestController
@RequestMapping("/api/validationCpf")
public class CpfValidationController {
	
	@Autowired
    private CpfValidationService cpfValidationService;

    @GetMapping("/{cpf}")
    public ResponseEntity<ResponseCpfValidationDto> validateCpf(@PathVariable String cpf) {

        String votingStatus = cpfValidationService.canVote();
        if ("ABLE_TO_VOTE".equals(votingStatus)) {
            return ResponseEntity.ok(new ResponseCpfValidationDto(votingStatus));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseCpfValidationDto(votingStatus));
        }
    }
}

