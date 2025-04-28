package com.desafiofullstask.votacao.controller;


import com.desafiofullstask.votacao.entity.Vote;
import com.desafiofullstask.votacao.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/api/vote")
public class VoteController {

    @Autowired
    private VoteService voteService;

    @GetMapping("/{id}")
    public ResponseEntity<Vote> get(
            @PathVariable("id") Integer id) {
        return ResponseEntity.ok().body(voteService.findVoteById(id));
    }

    @PostMapping
    public ResponseEntity<String> save(
            @RequestBody Vote vote
    ) {
        return ResponseEntity.ok().body(voteService.save(vote));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity delete(
            @PathVariable("id") Integer id
    ) {
        voteService.delete(id);
        return ResponseEntity.ok().build();
    }
}
