package com.desafiofullstask.votacao.repository;

import com.desafiofullstask.votacao.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface VoteRepository extends JpaRepository<Vote, Integer> {

    @Query("SELECT count(v) FROM Vote v WHERE v.discussId = :discussId AND v.vote = 'sim'")
    int countVotesYesByDiscussId(@Param("discussId") Integer discussId);

    @Query("SELECT count(v) FROM Vote v WHERE v.discussId = :discussId AND v.vote = 'não'")
    int countVotesNoByDiscussId(@Param("discussId") Integer discussId);

    Optional<Vote> findByAssociatedCpfAndDiscussId(String associatedCpf, Integer discussId);
}
