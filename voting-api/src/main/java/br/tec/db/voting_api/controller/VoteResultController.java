package br.tec.db.voting_api.controller;

import br.tec.db.voting_api.config.ApiVersion;
import br.tec.db.voting_api.dto.output.VoteResultOutputDTO;
import br.tec.db.voting_api.service.VoteResultService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiVersion.V1 + "/vote-result")
@RequiredArgsConstructor
@Tag(name = "Resultados", description = "Endpoints para obter os resultados da votação")
public class VoteResultController {

    private final VoteResultService voteResultService;

    @Operation(summary = "Obter os resultados da votação de uma pauta")
    @GetMapping("/{agendaId}")
    public ResponseEntity<VoteResultOutputDTO> getVowsResults(@PathVariable Long agendaId) {
        VoteResultOutputDTO voteResultOutputDTO = voteResultService.getVowsResults(agendaId);
        return ResponseEntity.ok(voteResultOutputDTO);
    }
}
