package db.server.app.domain.Rulling.Repository;

import db.server.app.domain.Rulling.Model.Rulling;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface RullingRepository extends MongoRepository<Rulling, String> {
    @Query("{'active': true, 'expiration': { $lt: ?0 }}")
    List<Rulling> findRullingsExpireds(LocalDateTime expiration);
}
