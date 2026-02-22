package dev.rodrigo.desafiovotacao.controller;

import dev.rodrigo.desafiovotacao.dto.PautaRequestDto;
import dev.rodrigo.desafiovotacao.dto.PautaResponseDto;
import dev.rodrigo.desafiovotacao.mappers.PautaMapper;
import dev.rodrigo.desafiovotacao.service.PautaService;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/v1/pautas")
@Tag(name = "Pautas", description = "Operações relacionadas às Pautas discutidas em Assembléia")
public class PautaController {

  private final PautaService service;
  private final PautaMapper mapper;

  @Operation(summary = "Consultar uma Pauta por ID")
  @GetMapping("/{id}")
  public PautaResponseDto buscarPautaPorId(@PathVariable Long id) {
    return mapper.toPautaResponseDto(service.buscarPautaPorId(id));
  }

  @Operation(summary = "Consultar todas as pautas cadastradas")
  @GetMapping()
  public List<PautaResponseDto> buscarPautas() {
    return mapper.toPautaResponseList(service.buscarPautas());
  }

  @Operation(summary = "Criar uma nova Pauta")
  @ApiResponses(
      value = {@ApiResponse(responseCode = "201", description = "Pauta criada com sucesso")})
  @PostMapping()
  @ResponseStatus(HttpStatus.CREATED)
  public void criarPauta(@RequestBody PautaRequestDto dto) {
    service.criarNovaPauta(dto);
  }
}
