package br.com.jardelkuhn.backend.repository;

import br.com.jardelkuhn.backend.entity.Agenda;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgendaRepository extends JpaRepository<Agenda, Long> {
}