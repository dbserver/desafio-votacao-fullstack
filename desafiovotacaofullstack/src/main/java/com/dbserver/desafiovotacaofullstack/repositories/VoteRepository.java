package com.dbserver.desafiovotacaofullstack.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dbserver.desafiovotacaofullstack.domains.Vote;

public interface VoteRepository extends JpaRepository<Vote, Integer> {
	
	@Query("select v from Vote v where v.id.session.id = :idSession")
	public List<Vote> findVoteBySession(@Param("idSession") Integer idSession);
	
	public long countByResponseAndIdSessionId(String responseVote, Integer sessionId);
	
	public boolean existsByIdSessionIdAndIdAssociateId(Integer idSession, Integer idAssociate);
}
