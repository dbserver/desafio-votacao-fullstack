package db.server.app.domain.Rulling.Services;

import db.server.app.commons.Enums.Vote;
import db.server.app.domain.Rulling.DTO.RullingDTO;
import db.server.app.domain.Rulling.Model.Rulling;
import db.server.app.domain.Rulling.Model.RullingVote;
import db.server.app.domain.Rulling.Repository.RullingRepository;
import db.server.app.domain.Rulling.Repository.RullingVoteRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Service
public class RullingService {
    private RullingRepository rullingRepository;
    private RullingVoteRepository rullingVoteRepository;

    public RullingService(RullingRepository rullingRepository, RullingVoteRepository rullingVoteRepository) {
        this.rullingRepository = rullingRepository;
        this.rullingVoteRepository = rullingVoteRepository;
    }

    public Optional<Rulling> save(RullingDTO rulling) throws Exception {
        Rulling newRulling = new Rulling();
        newRulling.setNewRulling(rulling.title(), rulling.description(), rulling.author(), rulling.expiration());
        newRulling = this.rullingRepository.save(newRulling);
        return Optional.of(newRulling);
    }

    public Optional<Rulling> getRullingById(String id) {
        return this.rullingRepository.findById(id);
    }

    private void createRullingVote(String rullingId, String userId, Vote vote) {
        RullingVote newRullingVote = new RullingVote();
        newRullingVote.setNewVote(rullingId, userId, vote);
        this.rullingVoteRepository.save(newRullingVote);
    }

    public void setRullingLikeInative(Rulling rulling) {
        rulling.setActive(false);
        this.rullingRepository.save(rulling);
    }

    public Optional<Rulling> addVoteYes(String id, String userId) {
        Optional<Rulling> rulling = getRullingById(id);
        if (rulling.isEmpty()) {
            return rulling;
        }

        Rulling rlg = rulling.get();

        Date now = new Date();

        if (now.compareTo(rlg.getExpiration()) > 0) {
            setRullingLikeInative(rlg);
            return Optional.empty();
        }

        rlg.increaseVoteYes();
        this.rullingRepository.save(rlg);

        createRullingVote(id, userId, Vote.YES);

        return Optional.of(rlg);
    }

    public Optional<Rulling> addVoteNo(String id, String userId) {
        Optional<Rulling> rulling = getRullingById(id);
        if (rulling.isEmpty()) {
            return rulling;
        }

        Rulling rlg = rulling.get();

        Date now = new Date();

        if (now.compareTo(rlg.getExpiration()) > 0) {
            setRullingLikeInative(rlg);
            return Optional.empty();
        }

        rlg.increaseVoteNo();
        this.rullingRepository.save(rlg);

        createRullingVote(id, userId, Vote.NO);

        return Optional.of(rlg);
    }

    public Boolean userAlreadyVoted(String rullingId, String userId) {
        return this.rullingVoteRepository.findByRullingIdAndUserId(rullingId, userId).isPresent();
    }

    public Long countRullings() {
        return this.rullingRepository.count();
    }

    public List<Rulling> getRullings(long page, long pageSize) {
        Pageable pageable = PageRequest.of(
                (int) page - 1,
                (int) pageSize,
                Sort.by("createdAt").descending()
        );
       Page<Rulling> response = this.rullingRepository.findAll(pageable);
       if (response.isEmpty()) {
           return List.of();
       }
       return response.getContent();
    }

}
