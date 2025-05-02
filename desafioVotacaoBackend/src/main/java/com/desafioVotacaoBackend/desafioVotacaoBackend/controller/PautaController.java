package com.desafioVotacaoBackend.desafioVotacaoBackend.controller;

import java.util.HashMap;
import java.util.List;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.desafioVotacaoBackend.desafioVotacaoBackend.model.Pauta;
import com.desafioVotacaoBackend.desafioVotacaoBackend.record.CriarPautaRecord;
import com.desafioVotacaoBackend.desafioVotacaoBackend.record.ListaPauta;
import com.desafioVotacaoBackend.desafioVotacaoBackend.service.PautaService;

@RestController
@RequestMapping("/api/pautas")
@CrossOrigin(origins = "*")
public class PautaController {
    @Autowired
    private PautaService service;

    @PostMapping
    public ResponseEntity<?> criarPauta(@RequestBody CriarPautaRecord request) {
        try {
            Pauta pauta = service.criarPauta(request.titulo(), request.descricao());

            Map<String, Object> resposta = new HashMap<>();
            resposta.put("status", 201);
            resposta.put("mensagem", "Pauta criada com sucesso!");

            return ResponseEntity.status(HttpStatus.CREATED).body(resposta);

        } catch (RuntimeException e) {
            Map<String, Object> erro = new HashMap<>();
            erro.put("status", 400);
            erro.put("mensagem", "Erro ao criar pauta.");
            erro.put("detalhe", e.getMessage());
            return ResponseEntity.badRequest().body(erro);
        }
    }

    @PostMapping("/{id}/abrir-sessao")
    public ResponseEntity<?> abrirSessao(
            @PathVariable Long id,
            @RequestParam(required = false) Integer minutos) {
        try {
            Pauta pauta = service.abrirSessao(id, minutos);

            Map<String, Object> resposta = new HashMap<>();
            resposta.put("status", 201);
            resposta.put("mensagem", "Sessão aberta com sucesso!");

            return ResponseEntity.status(HttpStatus.CREATED).body(resposta);

        } catch (RuntimeException e) {
            Map<String, Object> erro = new HashMap<>();
            erro.put("status", 400);
            erro.put("mensagem", "Erro ao abrir sessão.");
            erro.put("detalhe", e.getMessage());

            return ResponseEntity.badRequest().body(erro);
        }
    }

    @GetMapping
    public List<ListaPauta> findAlls() {
        return service.listarTodasPautas().stream().map(ListaPauta::new).toList();
    }

}
