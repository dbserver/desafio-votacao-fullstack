package com.db.desafiotecnico_fullstack.controllers;

import com.db.desafiotecnico_fullstack.models.dto.ResultadoDTO;
import com.db.desafiotecnico_fullstack.services.ResultadoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/pautas")
public class ResultadoController {

    private final ResultadoServiceImpl resultadoService;

    @Autowired
    public ResultadoController(ResultadoServiceImpl resultadoService) {
        this.resultadoService = resultadoService;
    }

    @GetMapping("/{pautaId}/resultado")
    public ResponseEntity<ResultadoDTO> getResultado(@PathVariable UUID pautaId) {
        try {
            ResultadoDTO resultado = resultadoService.calcularResultado(pautaId);
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}