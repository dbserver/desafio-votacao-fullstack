package com.example.votacao.voto;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.votacao.voto.dto.CriarVotoRequest;
import com.example.votacao.voto.dto.VotoResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Voto", description = "Votação em pauta")
@RestController
@RequestMapping("/api/v1/pauta/{pautaId}/voto")
public class VotoController {

  @Autowired
  private VotoService votoService;

  @Operation(summary = "Registra o voto de um associado na pauta")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Voto registrado"),
      @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
      @ApiResponse(responseCode = "404", description = "Sessão ou associado não encontrado", content = @Content),
      @ApiResponse(responseCode = "409", description = "Sessão encerrada ou associado já votou", content = @Content)
  })
  @PostMapping
  public ResponseEntity<VotoResponse> create(@PathVariable Long pautaId,
      @Valid @RequestBody CriarVotoRequest request) {
    Voto voto = votoService.create(pautaId, request);

    return ResponseEntity.status(HttpStatus.CREATED).body(VotoResponse.from(voto));
  }

  @Operation(summary = "Lista todos os votos pela pauta")
  @ApiResponse(responseCode = "200", description = "Lista de pautas")
  @GetMapping
  public ResponseEntity<List<VotoResponse>> listByPauta(@PathVariable Long pautaId) {
    List<VotoResponse> votos = votoService.getByPautaId(pautaId).stream()
        .map(VotoResponse::from)
        .toList();

    return ResponseEntity.ok(votos);
  }
}
