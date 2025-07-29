package com.nunesd66.pautas_api.voto.controller;

import com.nunesd66.pautas_api.core.dto.ApiResponse;
import com.nunesd66.pautas_api.voto.exception.VotoException;
import com.nunesd66.pautas_api.voto.service.VotoService;
import com.nunesd66.pautas_api.voto.model.Voto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/voto")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class VotoControllerImpl {

    private final VotoService service;

    @PostMapping("/votar")
    public ResponseEntity<ApiResponse<Void>> votar(@RequestBody Voto voto) {
        try {
            this.service.votar(voto);
            return ResponseEntity.noContent().build();
        } catch (VotoException | RuntimeException e) {
            ApiResponse<Void> respErro = new ApiResponse<>(null, e.getMessage());
            return ResponseEntity.badRequest().body(respErro);
        }
    }

}
