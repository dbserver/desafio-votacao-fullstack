package com.desafiofullstask.votacao.service;

import com.desafiofullstask.votacao.entity.Vote;
import com.desafiofullstask.votacao.repository.VoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class VoteServiceTest {

    @Autowired
    private VoteService voteService;

    @Autowired
    private VoteRepository voteRepository;

    private Vote vote;

    @BeforeEach
    void setup() {
        vote = new Vote();
        vote.setVote("sim");
        vote.setAssociatedCpf("841.318.726-50"); /** CPF valido gerado de forma aleatória */
        vote.setDiscussId(1);
        voteService.save(vote);
        vote = voteRepository.findByAssociatedCpfAndDiscussId("841.318.726-50", 1).get();
    }

    @Test
    void save() {
        String savedVote = voteService.save(vote);

        assertThat(savedVote.equals("ABLE_TO_VOTE"));
    }


    @Test
    void findVoteById() {
        Vote retrievedVote = voteService.findVoteById(1);
        assertThat(retrievedVote).isNotNull();
    }

    @Test
    void delete() {

        voteService.delete(vote.getId());

        Vote retrievedVote = voteRepository.findById(vote.getId()).orElse(null);
        assertThat(retrievedVote).isNull();
    }

}