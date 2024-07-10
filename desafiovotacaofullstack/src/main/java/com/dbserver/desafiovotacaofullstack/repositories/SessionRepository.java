package com.dbserver.desafiovotacaofullstack.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dbserver.desafiovotacaofullstack.domains.Session;

public interface SessionRepository extends JpaRepository<Session, Integer>{

}
