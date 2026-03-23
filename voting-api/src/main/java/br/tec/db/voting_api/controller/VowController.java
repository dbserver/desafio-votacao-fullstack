package br.tec.db.voting_api.controller;

import br.tec.db.voting_api.config.ApiVersion;
import br.tec.db.voting_api.dto.input.VowInputDTO;
import br.tec.db.voting_api.service.VowService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiVersion.V1 + "/vote")
@RequiredArgsConstructor
@Tag(name = "Votação", description = "Endpoints relacionados à votação")
public class VowController {

    private final VowService vowService;

    @PostMapping("/register")
    @Operation(summary = "Registrar voto em uma pauta")
    public ResponseEntity<Void> registerVote(@RequestBody VowInputDTO vowInputDTO) {
        vowService.registerVote(vowInputDTO);
        return ResponseEntity.ok().build();
    }
}
