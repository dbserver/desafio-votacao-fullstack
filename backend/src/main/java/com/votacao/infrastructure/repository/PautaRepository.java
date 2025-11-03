package com.votacao.infrastructure.repository;

import com.votacao.domain.model.Pauta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PautaRepository extends JpaRepository<Pauta, Long> {
}
