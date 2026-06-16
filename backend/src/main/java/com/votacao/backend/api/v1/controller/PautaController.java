package com.votacao.backend.api.v1.controller;

import com.votacao.backend.api.v1.dto.CriarPautaRequest;
import com.votacao.backend.api.v1.dto.PautaPageResponse;
import com.votacao.backend.api.v1.dto.PautaResponse;
import com.votacao.backend.domain.entity.Pauta;
import com.votacao.backend.domain.services.PautaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@Tag(name = "Pautas", description = "Gerenciamento de pautas")
@RestController
@RequestMapping("/api/v1/pautas")
@RequiredArgsConstructor
@Slf4j
public class PautaController {

    private final PautaService pautaService;

    @Operation(summary = "Criar nova pauta")
    @ApiResponse(responseCode = "201", description = "Pauta criada com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @PostMapping
    public ResponseEntity<PautaResponse> criar(@RequestBody @Valid CriarPautaRequest request) {
        Pauta pauta = pautaService.criar(request.titulo(), request.descricao());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(pauta.getId())
                .toUri();

        return ResponseEntity.created(location).body(PautaResponse.from(pauta));
    }

    @Operation(summary = "Listar pautas paginadas")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    @GetMapping
    public ResponseEntity<PautaPageResponse> listar(
            @PageableDefault(size = 6, sort = "criadoEm", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(PautaPageResponse.from(pautaService.listar(pageable)));
    }

    @Operation(summary = "Buscar pauta por ID")
    @ApiResponse(responseCode = "200", description = "Pauta encontrada")
    @ApiResponse(responseCode = "404", description = "Pauta não encontrada")
    @GetMapping("/{id}")
    public ResponseEntity<PautaResponse> buscar(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(PautaResponse.from(pautaService.buscarPorId(id)));
    }
}
