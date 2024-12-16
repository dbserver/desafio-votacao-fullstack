package com.votacao.desafiovotacao.application.controllers;

import com.votacao.desafiovotacao.domain.services.CPFValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/CPFvalidator")
public class CPFValidationController {

    @Autowired
    private CPFValidationService cpfValidationService;

    @GetMapping("")
    @CrossOrigin("*")
    public ResponseEntity<?> validateCpf(@RequestParam String cpf) {

        if(!cpfValidationService.isValid(cpf)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("CPF inválido");
        }

        String cpfStatus = cpfValidationService.isAbleToVote(cpf);
        Map<String, String> response = new HashMap<>();
        response.put("status", cpfStatus);
        if (cpfStatus.equals("ABLE_TO_VOTE")) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

    }
}

