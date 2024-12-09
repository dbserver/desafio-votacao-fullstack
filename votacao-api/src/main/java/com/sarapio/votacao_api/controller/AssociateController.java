package com.sarapio.votacao_api.controller;

import com.sarapio.votacao_api.domain.associate.Associate;
import com.sarapio.votacao_api.domain.associate.AssociateRequestDTO;
import com.sarapio.votacao_api.service.AssociateService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/associate")
public class AssociateController {

    private final AssociateService associateService;

    public AssociateController(AssociateService associateService) {
        this.associateService = associateService;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Associate> createAssociate(@RequestBody AssociateRequestDTO data) {
        Associate associate = associateService.createAssociate(data);
        return ResponseEntity.ok(associate);
    }
}
