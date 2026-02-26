package db.server.app.domain.Rulling.Repository;

import db.server.app.domain.Rulling.Model.RullingVote;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface RullingVoteRepository extends MongoRepository<RullingVote, String> {
    @Query("{ 'rullingId' : ?0, 'userId' : ?1 }")
    Optional<RullingVote> findByRullingIdAndUserId(String rullingId, String userId);
}
