package com.votacao.desafiovotacao.infra;

import com.votacao.desafiovotacao.domain.entities.Associated;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssociatedRepository extends MongoRepository<Associated, String> {
    Boolean findByCpf(String cpf);
}
