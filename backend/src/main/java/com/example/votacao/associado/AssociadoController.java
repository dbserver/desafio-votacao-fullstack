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

import com.example.votacao.associado.dto.AssociadoResponse;
import com.example.votacao.associado.dto.CriarAssociadoRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Associado", description = "Cadastro e listagem de associados")
@RestController
@RequestMapping("/api/v1/associado")
public class AssociadoController {

  @Autowired
  private AssociadoService associadoService;

  @Operation(summary = "Cria um novo associado")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Associado criado"),
      @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content)
  })
  @PostMapping
  public ResponseEntity<AssociadoResponse> create(@Valid @RequestBody CriarAssociadoRequest request) {
    Associado associado = associadoService.create(request);

    return ResponseEntity.status(HttpStatus.CREATED).body(AssociadoResponse.from(associado));
  }

  @Operation(summary = "Lista todos os associados")
  @ApiResponse(responseCode = "200", description = "Lista de associados")
  @GetMapping
  public ResponseEntity<List<AssociadoResponse>> listAll() {
    List<AssociadoResponse> associados = associadoService.getAllAssociados().stream()
        .map(AssociadoResponse::from)
        .toList();

    return ResponseEntity.ok(associados);
  }

}
