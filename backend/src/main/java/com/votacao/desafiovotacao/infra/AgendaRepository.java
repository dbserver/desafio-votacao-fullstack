package com.votacao.desafiovotacao.infra;

import com.votacao.desafiovotacao.domain.entities.Agenda;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgendaRepository extends MongoRepository<Agenda, String> {
}
