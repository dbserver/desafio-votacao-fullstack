package com.sousadev.voteapp.controller;

import com.sousadev.voteapp.dtos.CpfValidationResponseDto;
import com.sousadev.voteapp.enums.CpfVoteStatus;
import com.sousadev.voteapp.utils.CpfValidatorUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cpf")
public class CpfController {

    @GetMapping("/{cpf}")
    public ResponseEntity<CpfValidationResponseDto> validate(
            @PathVariable String cpf
    ) {
        CpfVoteStatus status = CpfValidatorUtil.validate(cpf);
        CpfValidationResponseDto response = new CpfValidationResponseDto(status);

        if (status == CpfVoteStatus.UNABLE_TO_VOTE) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        return ResponseEntity.ok(response);
    }
}
