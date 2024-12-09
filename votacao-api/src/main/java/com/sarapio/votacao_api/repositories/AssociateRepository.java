package com.sarapio.votacao_api.repositories;

import com.sarapio.votacao_api.domain.associate.Associate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AssociateRepository extends JpaRepository<Associate, UUID> {
    Optional<Associate> findByCpf(String cpf);
}