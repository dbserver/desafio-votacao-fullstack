package com.example.votacao.associado;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.votacao.associado.dto.CriarAssociadoRequest;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/associado")
public class AssociadoController {

  @Autowired
  private AssociadoService associadoService;

  @PostMapping
  private ResponseEntity<Associado> create(@Valid @RequestBody CriarAssociadoRequest request) {
    Associado associado = associadoService.create(request);

    return ResponseEntity.status(HttpStatus.CREATED).body(associado);
  }

  @GetMapping
  private ResponseEntity<List<Associado>> listAll() {
    List<Associado> associados = associadoService.getAllAssociados();

    return ResponseEntity.ok(associados);
  }

}
