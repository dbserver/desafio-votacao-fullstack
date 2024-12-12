package com.db.desafiotecnico_fullstack.controllers;


import com.db.desafiotecnico_fullstack.models.Pauta;
import com.db.desafiotecnico_fullstack.models.dto.PautaDTO;
import com.db.desafiotecnico_fullstack.services.PautaServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/pautas")
public class PautaController {
    private final PautaServiceImpl pautaService;

    @Autowired
    public PautaController(PautaServiceImpl pautaService) {
        this.pautaService = pautaService;
    }

    @PostMapping //endpoint para criar nova pauta
    public ResponseEntity<PautaDTO> criarPauta(@RequestBody @Valid PautaDTO pautaDTO) {
        Pauta pauta = pautaService.criarPauta(pautaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(new PautaDTO(pauta));
    }

    @PostMapping("/{id}/abrir-sessao") //enpoint para abrir sessao de votacao para uma pauta
    public ResponseEntity<String> abrirSessao
            (@PathVariable UUID id, @RequestParam(required = false) Integer tempoVotacao) {
        pautaService.abrirSessao(id, tempoVotacao);
        return ResponseEntity.ok("Sessão de votação aberta com sucesso");
    }

    @GetMapping("/{id}")
    public ResponseEntity<PautaDTO> buscarPauta(@PathVariable UUID id) {
        Pauta pauta = pautaService.buscarPorId(id);
        return ResponseEntity.ok(new PautaDTO(pauta));
    }

}
