package com.dbserver.desafiovotacaofullstack.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dbserver.desafiovotacaofullstack.domains.Agenda;

public interface AgendaRepository extends JpaRepository<Agenda, Integer> {

}
