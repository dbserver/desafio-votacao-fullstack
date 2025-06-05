package br.dev.ederson.spring.cooperativa.repository;

import br.dev.ederson.spring.cooperativa.model.Associate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssociateRepository extends JpaRepository<Associate, Long> {
    Associate getAssociateByDocument(String document);
}
