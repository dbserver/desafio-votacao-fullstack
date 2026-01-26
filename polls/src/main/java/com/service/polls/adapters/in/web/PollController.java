package com.service.polls.adapters.in.web;

import com.service.polls.adapters.out.ExternalDocumentProvider;
import com.service.polls.adapters.out.persistence.entities.PollEntity;
import com.service.polls.application.dto.*;
import com.service.polls.domain.model.Poll;
import com.service.polls.ports.in.*;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(
        origins = "http://localhost:5173",
        allowedHeaders = "*"
)
@RestController
@RequestMapping("/polls")
public class PollController {

    private final ICreatePollUseCase createPollUseCase;
    private final IListPollsUseCase listPollsUseCase;
    private final IVoteUseCase voteUseCase;
    private final IGetPollsUseCase getPollsUseCase;
    private final IGetPollResultUseCase pollResultUseCase;

    private final ExternalDocumentProvider externalDocumentProvider;

    public PollController(ICreatePollUseCase createPollUseCase, IListPollsUseCase listPollsUseCase, IVoteUseCase voteUseCase, IGetPollsUseCase getPollsUseCase, IGetPollResultUseCase pollResultUseCase, ExternalDocumentProvider externalDocumentProvider) {
        this.createPollUseCase = createPollUseCase;
        this.listPollsUseCase = listPollsUseCase;
        this.voteUseCase = voteUseCase;
        this.getPollsUseCase = getPollsUseCase;
        this.pollResultUseCase = pollResultUseCase;
        this.externalDocumentProvider = externalDocumentProvider;
    }

    @PostMapping
    public ResponseEntity<Poll> create(@RequestBody PollDTO pollDTO) {
        Poll poll = createPollUseCase.execute(pollDTO);
        return ResponseEntity.ok(poll);
    }

    @GetMapping
    public Page<PollEntity> findAll( @RequestParam(defaultValue = "1") int page,
                                     @RequestParam(defaultValue = "10") int size) {
        return listPollsUseCase.execute(page, size);
    }

    @GetMapping("/{pollId}")
    public ResponseEntity<Poll> findById( @PathVariable("pollId") Long pollId) {
        Poll poll = getPollsUseCase.execute(pollId);
        return ResponseEntity.ok(poll);
    }

    @GetMapping("/{pollId}/result")
    public ResponseEntity<PollResultDTO> result(@PathVariable("pollId") Long pollId) {
        PollResultDTO poll = pollResultUseCase.execute(pollId);
        return ResponseEntity.ok(poll);
    }

    @PostMapping("/vote")
    public ResponseEntity<VotedDTO> vote(@RequestBody @Valid VoteDTO voteDTO) {
        voteUseCase.execute(voteDTO);
        return ResponseEntity.ok(VotedDTO.builder().message("voto realizado com sucesso!").build());
    }

    @GetMapping("/documents/{document}")
    public ResponseEntity<DocumentValidatedDTO> check(@PathVariable("document") String document) {
        boolean isValid = externalDocumentProvider.validateCPF(document);
        if(!isValid) {
            return ResponseEntity.status(404).body(DocumentValidatedDTO.builder().status("UNABLE_TO_VOTE").build());
        }
        return ResponseEntity.ok(DocumentValidatedDTO.builder().status("ABLE_TO_VOTE").build());
    }
}
