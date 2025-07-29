package com.nunesd66.pautas_api.pauta.controller;

import com.nunesd66.pautas_api.core.dto.ApiResponse;
import com.nunesd66.pautas_api.pauta.dto.PautaCreateRequest;
import com.nunesd66.pautas_api.pauta.dto.PautaResponse;
import com.nunesd66.pautas_api.pauta.exception.PautaException;
import com.nunesd66.pautas_api.pauta.model.Pauta;
import com.nunesd66.pautas_api.pauta.service.PautaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController()
@RequestMapping("/pauta")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class PautaControllerImpl {

    private final PautaService pautaService;

    @GetMapping()
    public ResponseEntity<ApiResponse<List<PautaResponse>>> getAll() {
        return ResponseEntity.ok(
                new ApiResponse<>(this.pautaService.getAll(), null)
        );
    }

    @GetMapping("/last-record")
    public ResponseEntity<ApiResponse<PautaResponse>> getLastRecord() {
        return ResponseEntity.ok(
            new ApiResponse<>(this.pautaService.getLastRecord(), null)
        );
    }

    @PostMapping()
    public ResponseEntity<ApiResponse<PautaResponse>> save(@RequestBody PautaCreateRequest request) {
        try {
            return ResponseEntity.ok(
                new ApiResponse<>(this.pautaService.save(request), null)
            );
        } catch (PautaException | RuntimeException e) {
            ApiResponse<PautaResponse> apiErr = new ApiResponse<>(null, e.getMessage());
            return ResponseEntity.badRequest().body(apiErr);
        }
    }

    @PutMapping("/fechar-votos/{id}")
    public ResponseEntity<ApiResponse<PautaResponse>> fecharVotos(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(
                new ApiResponse<>(this.pautaService.fecharVotos(id), null)
            );
        } catch (PautaException e) {
            ApiResponse<PautaResponse> apiErr = new ApiResponse<>(null, e.getMessage());
            return ResponseEntity.badRequest().body(apiErr);
        }
    }

}
