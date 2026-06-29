package com.sousadev.voteapp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.sousadev.voteapp.dtos.AssociateDto;
import com.sousadev.voteapp.entity.Associate;
import com.sousadev.voteapp.service.AssociateService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/associate")
@RequiredArgsConstructor
public class AssociateController {
    private final AssociateService associateService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Associate create(
            @RequestBody AssociateDto request) {
        return associateService.create(request);
    }
}
