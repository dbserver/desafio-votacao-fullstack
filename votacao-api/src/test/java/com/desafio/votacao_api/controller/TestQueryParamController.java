package com.desafio.votacao_api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test-params")
class TestQueryParamController {

    @GetMapping
    ResponseEntity<String> requiredParam(@RequestParam String required) {
        return ResponseEntity.ok(required);
    }
}

