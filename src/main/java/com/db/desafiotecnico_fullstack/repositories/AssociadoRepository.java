package com.db.desafiotecnico_fullstack.repositories;

import com.db.desafiotecnico_fullstack.models.Associado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface AssociadoRepository extends JpaRepository<Associado, UUID> {
}