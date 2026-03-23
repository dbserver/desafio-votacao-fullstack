package com.desafio.votacao_api.repository;

import com.desafio.votacao_api.model.Pauta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository interface for persisting and retrieving Pauta entities.
 */
@Repository
public interface PautaRepository extends JpaRepository<Pauta, UUID> {

    /**
     * Finds all active agendas.
     *
     * @return a list of all agendas(pauta) with isActive set to true
     */
    @Query("SELECT p FROM Pauta p WHERE p.isActive = true")
    List<Pauta> findAllByIsActiveTrue();
}


