package com.example.votacao.pauta;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.votacao.pauta.dto.CriarPautaRequest;
import com.example.votacao.pauta.dto.PautaResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Pauta", description = "Cadastro de pauta")
@RestController
@RequestMapping("/api/v1/pauta")
public class PautaController {

  @Autowired
  private PautaService pautaService;

  @Operation(summary = "Cria uma nova pauta")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Pauta criada"),
      @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content)
  })
  @PostMapping
  public ResponseEntity<PautaResponse> create(@Valid @RequestBody CriarPautaRequest request) {
    Pauta associado = pautaService.create(request);

    return ResponseEntity.status(HttpStatus.CREATED).body(PautaResponse.from(associado));
  }

  @Operation(summary = "Lista todas as pautas")
  @ApiResponse(responseCode = "200", description = "Lista de pautas")
  @GetMapping
  public ResponseEntity<List<PautaResponse>> listAll() {
    List<PautaResponse> associados = pautaService.getAllPautas().stream()
        .map(PautaResponse::from)
        .toList();

    return ResponseEntity.ok(associados);
  }
}
