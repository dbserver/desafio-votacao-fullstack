package db.server.app.domain.Rulling.Controllers;

import db.server.app.commons.Records.Http.HttpErrorResponse;
import db.server.app.domain.Rulling.DTO.NewRullingDTO;
import db.server.app.domain.Rulling.DTO.RullingCountDTO;
import db.server.app.domain.Rulling.DTO.RullingDTO;
import db.server.app.domain.Rulling.Model.Rulling;
import db.server.app.domain.Rulling.Services.RullingService;
import db.server.app.domain.User.Model.User;
import db.server.app.domain.User.Services.AuthService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;


@RestController
@RequestMapping("/rulling")
public class RullingController {
    @Value("${spring.pagination.size}")
    private Long pageSize;

    private final RullingService rullingService;
    private final AuthService authService;

    public RullingController(RullingService rullingService, AuthService authService) {
        this.rullingService = rullingService;
        this.authService = authService;
    }

    @PostMapping
    public ResponseEntity SaveRulling(
            @RequestBody NewRullingDTO rullingDTO
    ) throws Exception {
       User authUser = this.authService.getSessionUser();
       Date now = new Date();
       now.setTime(now.getTime() + 1000 * 60);

       if (rullingDTO.expiration().isPresent() && !rullingDTO.expiration().get().isEmpty()) {
           Instant expirationInstant = Instant.parse(rullingDTO.expiration().get());
           now = Date.from(expirationInstant);

           if (now.before(new Date())) {
               return ResponseEntity.
                       status(HttpStatus.BAD_REQUEST).
                       body(new HttpErrorResponse(HttpStatus.BAD_REQUEST.value(), Optional.of("Rulling expired")));
           }
       }

        Optional<Rulling> newRulling = this.
                rullingService.
                save(new RullingDTO(rullingDTO.title(), rullingDTO.description(), authUser.getId(), now));

        if (newRulling.isEmpty()) {
            return ResponseEntity.
                    status(HttpStatus.BAD_REQUEST).
                    body(new HttpErrorResponse(HttpStatus.BAD_REQUEST.value(), Optional.of("Rulling can't be saved")));
        }

        return ResponseEntity.ok(newRulling.get());
    }

    @PatchMapping("/{id}/vote/yes")
    public ResponseEntity VoteYes(@PathVariable String id) throws Exception {
        User authUser = this.authService.getSessionUser();

        Boolean userAlreadyVoted = this.rullingService.userAlreadyVoted(id, authUser.getId());

        if (userAlreadyVoted) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new HttpErrorResponse(HttpStatus.CONFLICT.value(), Optional.of("You already voted")));
        }

        Optional<Rulling> rulling = this.rullingService.addVoteYes(id, authUser.getId());
        if (rulling.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new HttpErrorResponse(HttpStatus.NOT_FOUND.value(), Optional.of("Rulling not found or expired")));
        }

        return ResponseEntity.ok(rulling.get());
    }

    @PatchMapping("/{id}/vote/no")
    public ResponseEntity VoteNo(@PathVariable String id) throws Exception {
        User authUser = this.authService.getSessionUser();

        Boolean userAlreadyVoted = this.rullingService.userAlreadyVoted(id, authUser.getId());

        if (userAlreadyVoted) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new HttpErrorResponse(HttpStatus.CONFLICT.value(), Optional.of("You already voted")));
        }

        Optional<Rulling> rulling = this.rullingService.addVoteNo(id, authUser.getId());
        if (rulling.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new HttpErrorResponse(HttpStatus.NOT_FOUND.value(), Optional.of("Rulling not found or expired")));
        }

        return ResponseEntity.ok(rulling.get());
    }

    @GetMapping("/navigate/info")
    private ResponseEntity GetRullingInfo() {
        Long count = this.rullingService.countRullings();
        double pages = Math.ceil((double) count / pageSize);
        RullingCountDTO response = new RullingCountDTO(
                count,
                (long) pages
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all/{page}")
    private ResponseEntity GetRullings(
            @PathVariable Long page
    ) {
        return ResponseEntity.ok(this.rullingService.getRullings(page, pageSize));
    }
}
