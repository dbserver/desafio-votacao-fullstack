package br.tec.db.voting_api.controller;

import br.tec.db.voting_api.config.ApiVersion;
import br.tec.db.voting_api.dto.input.SessionInputDTO;
import br.tec.db.voting_api.service.SessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiVersion.V1 + "/session")
@RequiredArgsConstructor
@Tag(name = "Sessão", description = "Endpoints para gerenciamento as sessões")
public class SessionController {

    private final SessionService sessionService;

    @Operation(summary = "Abrir uma nova sessão em uma pauta")
    @PostMapping("/{agendaId}/open")
    public ResponseEntity<Void> openSession(@PathVariable Long agendaId, @RequestBody SessionInputDTO sessionInputDTO) {
        sessionService.openSession(agendaId, sessionInputDTO);
        return ResponseEntity.ok().build();
    }
}
