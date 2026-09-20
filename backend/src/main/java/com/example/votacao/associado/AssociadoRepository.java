package com.example.votacao.associado;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AssociadoRepository extends JpaRepository<Associado, Long> {
  boolean existsByCpf(String cpf);

  Optional<Associado> findByCpf(String cpf);
}
