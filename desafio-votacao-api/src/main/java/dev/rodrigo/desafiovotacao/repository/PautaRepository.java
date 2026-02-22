package dev.rodrigo.desafiovotacao.repository;

import dev.rodrigo.desafiovotacao.entity.Pauta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PautaRepository extends JpaRepository<Pauta, Long> {}
