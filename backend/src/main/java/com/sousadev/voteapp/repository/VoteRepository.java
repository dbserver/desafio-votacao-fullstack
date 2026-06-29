package com.sousadev.voteapp.repository;

import com.sousadev.voteapp.entity.Vote;
import com.sousadev.voteapp.enums.VoteValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface VoteRepository extends JpaRepository<Vote, UUID> {

    Optional<Vote> findByAssociateIdAndPautaId(UUID associateId, UUID pautaId);

    @Query("SELECT COUNT(v) FROM Vote v WHERE v.pauta.id = :pautaId AND v.value = :value")
    long countByPautaIdAndValue(@Param("pautaId") UUID pautaId, @Param("value") VoteValue value);

    List<Vote> findByPautaId(UUID pautaId);
}
