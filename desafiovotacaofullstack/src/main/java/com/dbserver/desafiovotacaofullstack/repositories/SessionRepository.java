package com.dbserver.desafiovotacaofullstack.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dbserver.desafiovotacaofullstack.domains.Agenda;
import com.dbserver.desafiovotacaofullstack.domains.Session;

public interface SessionRepository extends JpaRepository<Session, Integer>{
	
	public Session findByAgenda(Agenda agenda);
	
	@Query("select s from Session s where s.agenda.id = :idAgenda")
	public Session findByIdAgenda(@Param("idAgenda") Integer idAgenda);
	
	public boolean existsByAgenda(Agenda agenda);
}
