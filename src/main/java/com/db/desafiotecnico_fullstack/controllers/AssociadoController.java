package com.db.desafiotecnico_fullstack.controllers;

import com.db.desafiotecnico_fullstack.models.Associado;
import com.db.desafiotecnico_fullstack.models.dto.AssociadoDTO;
import com.db.desafiotecnico_fullstack.services.AssociadoServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/associados")
public class AssociadoController {

    @Autowired
    private AssociadoServiceImpl associadoService;

    @PostMapping
    public ResponseEntity<Associado> criar(@RequestBody @Valid AssociadoDTO associadoDTO) {
        Associado novoAssociado = associadoService.criar(associadoDTO);
        return new ResponseEntity<>(novoAssociado, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Associado>> listarTodos() {
        List<Associado> associados = associadoService.listarTodos();
        return ResponseEntity.ok(associados);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Associado> buscarPorId(@PathVariable UUID id) {
        Associado associado = associadoService.buscarPorId(id);
        return ResponseEntity.ok(associado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        associadoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Associado> atualizar(@PathVariable UUID id,
                                               @RequestBody @Valid AssociadoDTO associadoDTO) {
        Associado associadoAtualizado = associadoService.atualizar(id, associadoDTO);
        return ResponseEntity.ok(associadoAtualizado);
    }
}