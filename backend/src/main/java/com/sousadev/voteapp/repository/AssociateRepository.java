package com.sousadev.voteapp.repository;

import com.sousadev.voteapp.entity.Associate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AssociateRepository extends JpaRepository<Associate, UUID> {

    Optional<Associate> findByDocument(String document);


}
