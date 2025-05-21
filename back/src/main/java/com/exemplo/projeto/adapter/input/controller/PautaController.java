package com.exemplo.projeto.adapter.input.controller;

import com.exemplo.projeto.adapter.output.entity.PautaEntity;
import com.exemplo.projeto.adapter.output.mapper.PautaMapper;
import com.exemplo.projeto.adapter.input.dto.PautaRequest;
import com.exemplo.projeto.adapter.input.dto.PautaResponseDTO;
import com.exemplo.projeto.domain.model.Pauta;
import com.exemplo.projeto.application.port.input.PautaUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/pautas")
@RequiredArgsConstructor
@Tag(name = "Pautas", description = "Gerenciamento de pautas")
public class PautaController {
    private final PautaUseCase pautaUseCase;
    private  final PautaMapper pautaMapper;

    @Operation(summary = "Listar todas as pautas")
    @ApiResponse(responseCode = "200", description = "Pautas listadas com sucesso")
    @GetMapping
    public ResponseEntity<List<PautaResponseDTO>> listarPautas() {
        List<Pauta> pautas = pautaUseCase.buscar();

        if (pautas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<PautaResponseDTO> dtoList = pautas.stream()
                .map(pautaMapper::toDto)
                .toList();

        return ResponseEntity.ok(dtoList);
    }

    @Operation(summary = "Buscar uma pauta pelo ID")
    @ApiResponse(responseCode = "200", description = "Pauta encontrada")
    @ApiResponse(responseCode = "404", description = "Pauta não encontrada")
    @GetMapping("/{id}")
    public ResponseEntity<PautaResponseDTO> buscarPautasPorId(@PathVariable Long id) {
        Optional<Pauta> pautaOptional = pautaUseCase.buscarPorId(id);

        if (pautaOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        PautaResponseDTO dto = pautaMapper.toDto(pautaOptional.get());
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Criar uma nova pauta")
    @ApiResponse(responseCode = "200", description = "Pauta criada com sucesso")
    @PostMapping
    public ResponseEntity<PautaResponseDTO> criar(@RequestBody PautaRequest req) {
        PautaEntity pautaEntity = new PautaEntity();
        pautaEntity.setTitulo(req.getTitulo());
        pautaEntity.setDescricao(req.getDescricao());

        Pauta pauta = pautaMapper.toDomain(pautaEntity);
        Pauta pautaCriada = pautaUseCase.criar(pauta);

        return ResponseEntity.ok(new PautaResponseDTO(
                pautaCriada.getId(),
                pautaCriada.getTitulo(),
                pautaCriada.getDescricao(),
                pautaCriada.getSessao().getInicio(),
                pautaCriada.getSessao().getFim()
        ));
    }
}
