package db.server.app.domain.Rulling.services;

import db.server.app.commons.Enums.Vote;
import db.server.app.domain.Rulling.DTO.RullingDTO;
import db.server.app.domain.Rulling.Model.Rulling;
import db.server.app.domain.Rulling.Model.RullingVote;
import db.server.app.domain.Rulling.Repository.RullingRepository;
import db.server.app.domain.Rulling.Repository.RullingVoteRepository;
import db.server.app.domain.Rulling.Services.RullingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RullingServiceTest {
    @Mock
    private RullingRepository rullingRepository;

    @Mock
    private RullingVoteRepository rullingVoteRepository;

    @InjectMocks
    private RullingService rullingService;

    @Test
    void checkCreateRulling() throws Exception {
        Date now = new Date();
        now.setTime(now.getTime() + 1000 * 60);
        RullingDTO request = new RullingDTO("Title", "Description", "123", now);
        Rulling rulling = new Rulling();
        rulling.setNewRulling(request.title(), request.description(), request.author(), now);

        when(rullingRepository.save(any(Rulling.class))).thenReturn(rulling);

        Optional<Rulling> response = rullingService.save(request);

        assertEquals(Optional.of(rulling), response);
    }

    @Test
    void checkGetBullingById() throws Exception {
        String id = "123";
        Rulling rulling = new Rulling();
        rulling.setNewRulling("Title", "Description", "123", new Date());

        when(rullingRepository.findById(any(String.class))).thenReturn(Optional.of(rulling));
        Optional<Rulling> newRulling = rullingService.getRullingById(id);
        assertEquals(Optional.of(rulling), newRulling);
    }

    @Test
    void checkSetLikeInative() throws Exception {
        Rulling rulling = new Rulling();
        when(rullingRepository.save(any(Rulling.class))).thenReturn(rulling);
        rullingService.setRullingLikeInative(rulling);
    }

    @Test
    void checkAddVotes() {
        Rulling rulling = new Rulling();
        RullingVote voteYes = new RullingVote();

        Date now = new Date();
        now.setTime(now.getTime() + 1000 * 60);

        rulling.setNewRulling("Title", "Description", "123", now);
        voteYes.setNewVote("123", "123", Vote.YES);

        when(rullingRepository.findById(anyString())).thenReturn(Optional.of(rulling));
        when(rullingVoteRepository.save(any(RullingVote.class))).thenReturn(voteYes);

        Optional<Rulling> newRulling = rullingService.addVoteYes("123", "123");
        assertEquals(newRulling.get().getVoteYes(), 1);
        assertEquals(newRulling.get().getVoteTotal(), 1);

        newRulling = rullingService.addVoteNo("123", "123");
        assertEquals(newRulling.get().getVoteNo(), 1);
        assertEquals(newRulling.get().getVoteTotal(), 2);
    }

    @Test
    void checkIfYouCanVote() {
        RullingVote voteYes = new RullingVote();
        voteYes.setNewVote("123", "123", Vote.YES);
        when(rullingVoteRepository.findByRullingIdAndUserId(anyString(), anyString())).thenReturn(Optional.of(voteYes));

        Boolean vote = rullingService.userAlreadyVoted("123", "123");
        assertEquals(true, vote);
    }

    @Test
    void checkIfYouCantVote() {
        when(rullingVoteRepository.findByRullingIdAndUserId(anyString(), anyString())).thenReturn(Optional.empty());

        Boolean vote = rullingService.userAlreadyVoted("123", "123");
        assertEquals(false, vote);
    }

    @Test
    void checkGetRullings() {
        Rulling rulling = new Rulling();
        rulling.setNewRulling("Title", "Description", "123", new Date());
        Page<Rulling> page = new PageImpl<>(java.util.Arrays.asList(rulling));
        List<Rulling> rullings = page.getContent();

        when(rullingRepository.findAll(any(Pageable.class))).thenReturn(page);
        List<Rulling> response = rullingService.getRullings(1, 10);
        assertEquals(rullings, response);
        assertEquals(1, response.size());
    }

}
