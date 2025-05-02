package com.desafioVotacaoBackend.desafioVotacaoBackend.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.desafioVotacaoBackend.desafioVotacaoBackend.model.Associado;
import com.desafioVotacaoBackend.desafioVotacaoBackend.record.CriarAssociadoRecord;
import com.desafioVotacaoBackend.desafioVotacaoBackend.service.AssociadoService;

@RestController
@RequestMapping("/api/associado")
@CrossOrigin(origins = "*")
public class AssociadoController {

    @Autowired
    private AssociadoService service;

    @PostMapping
    public ResponseEntity<?> criarAssociado(@RequestBody CriarAssociadoRecord request) {
        try {
            Associado ass = service.cadastrarAssociado(request.nome(), request.cpf());

            Map<String, Object> resposta = new HashMap<>();
            resposta.put("status", 201);
            resposta.put("mensagem", "Associado criado com sucesso!");

            return ResponseEntity.status(HttpStatus.CREATED).body(resposta);

        } catch (RuntimeException e) {
            Map<String, Object> erro = new HashMap<>();
            erro.put("status", 400);
            erro.put("mensagem", "Erro ao criar associado.");
            erro.put("detalhe", e.getMessage());

            return ResponseEntity.badRequest().body(erro);
        }
    }

}
