package com.exemplo.projeto.adapter.output.repository;

import com.exemplo.projeto.adapter.output.entity.SessaoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpringSessaoRepository extends JpaRepository<SessaoEntity, Long> {

    Optional<SessaoEntity> findByIdPauta(Long idPauta);

}
