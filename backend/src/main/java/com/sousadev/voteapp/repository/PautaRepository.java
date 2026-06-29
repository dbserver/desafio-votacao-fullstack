package com.sousadev.voteapp.repository;

import com.sousadev.voteapp.entity.Pauta;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PautaRepository extends JpaRepository<Pauta, UUID> {
    @Override
    @EntityGraph(attributePaths = { "votes", "votes.associate" })
    List<Pauta> findAll();

    @Override
    @EntityGraph(attributePaths = { "votes", "votes.associate" })
    Optional<Pauta> findById(UUID uuid);
}