package com.desafio.votacao_api.repository;

import com.desafio.votacao_api.model.Pauta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PautaRepository extends JpaRepository<Pauta, UUID> {
}
