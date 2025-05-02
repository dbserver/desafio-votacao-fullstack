package com.desafioVotacaoBackend.desafioVotacaoBackend.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.desafioVotacaoBackend.desafioVotacaoBackend.record.CriarVotoRecord;
import com.desafioVotacaoBackend.desafioVotacaoBackend.service.VotoService;

@RestController
@RequestMapping("/api/votos")
@CrossOrigin(origins = "*")
public class VotoController {

    @Autowired
    private VotoService votoService;

    @PostMapping
    public ResponseEntity<?> registrarVoto(@RequestBody CriarVotoRecord request) {
        try {
            votoService.registrarVoto(
                    request.pautaId(),
                    request.cpf(),
                    request.opcao());

            Map<String, Object> response = new HashMap<>();
            response.put("status", 201);
            response.put("mensagem", "Voto adicionado com sucesso!");

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (RuntimeException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("status", 400);
            error.put("erro", e.getMessage());

            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/contabilizar/{pautaId}")
    public ResponseEntity<?> contabilizarResultado(@PathVariable Long pautaId) {
        try {
            Map<String, Long> resultado = votoService.contabilizarResultado(pautaId);

            if (resultado == null || resultado.isEmpty()) {
                Map<String, Object> erro = new HashMap<>();
                erro.put("status", 404);
                erro.put("mensagem", "Pauta não encontrada.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
            }

            return ResponseEntity.ok(resultado);

        } catch (Exception e) {
            Map<String, Object> erro = new HashMap<>();
            erro.put("status", 500);
            erro.put("mensagem", "Erro ao contabilizar votos.");
            erro.put("detalhe", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro);
        }
    }

}
